package alexo.ecommerce_api.service.internal.inventory.product_stock_management;

import alexo.ecommerce_api.cache.inventory.warehouse.WarehouseCacheService;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.ProductStockUpdateRequestDTO;
import alexo.ecommerce_api.entity.catalog.Product;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.entity.inventory.ProductWarehouseStock;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransaction;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import alexo.ecommerce_api.exception.persistance.UserNotFoundException;
import alexo.ecommerce_api.exception.service.inventory.product_stock_management.StockUpdateException;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseStockTransactionRepository;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
@Valid
public class TransactionalProductStockService {

    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseCacheService warehouseCacheService;
    private final ProductWarehouseStockRepository productWarehouseStockRepository;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;
    private final WarehouseStockTransactionRepository warehouseStockTransactionRepository;

    @Transactional
    protected WarehouseStockTransaction updateProductStockOnWarehouse(@NotNull ProductStockUpdateRequestDTO request) throws RuntimeException {
        int delta = request.deltaQuantity();

        WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode purposeCode = request.purposeCode();

        WarehouseStockTransactionPurposeType.WarehouseStockTransactionOperationCode operationCode = delta > 0
                ? WarehouseStockTransactionPurposeType.WarehouseStockTransactionOperationCode.INCREASE
                : WarehouseStockTransactionPurposeType.WarehouseStockTransactionOperationCode.DECREASE;

        if (!Arrays.asList(purposeCode.getAllowedOperations()).contains(operationCode)) {
            throw StockUpdateException.operationIsNotAllowedForThisPurpose(
                    purposeCode,
                    operationCode,
                    request.productId(),
                    request.warehouseId()
            );
        }

        ProductWarehouseStock stock = productWarehouseStockRepository
                .findByProductIdAndWarehouseIdForUpdate(
                        request.productId(),
                        request.warehouseId()
                )
                .orElse(null);

        WarehouseStockTransactionPurposeType purposeType = Optional.ofNullable(warehouseCacheService.getWarehouseStockTransactionPurposeTypes().get(request.purposeCode()))
                .orElseThrow(() -> new EntityNotFoundException("transaction purpose with code [" + request.purposeCode() + "] is not found"));

        Long userId = authorizationService.getCurrentUserPrincipalFromAuthentication().getId();

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> UserNotFoundException.fromUserId(userId));

        Product product = productRepository
                .findById(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("product with id [" + request.productId() + "] is not found"));

        Warehouse warehouse = warehouseRepository
                .findById(request.warehouseId())
                .orElseThrow(() -> new EntityNotFoundException("warehouse with id [" + request.warehouseId() + "] is not found"));

        int oldQuantity = stock == null
                ? 0
                : stock.getPhysicalQuantity();
        int newQuantity = oldQuantity + delta;

        if (newQuantity < 0) {
            throw StockUpdateException.stockDecreaseIsGreaterThanCurrentPhysicalQuantity(
                    request.deltaQuantity(),
                    oldQuantity,
                    request.productId(),
                    request.warehouseId()
            );
        }

        if (stock != null && newQuantity < stock.getReservedQuantity()) {
            throw StockUpdateException.stockDecreaseResultIsLessThanCurrentReserves(
                    newQuantity,
                    request.deltaQuantity(),
                    stock.getPhysicalQuantity(),
                    request.productId(),
                    request.warehouseId()
            );
        }

        if (stock == null) {
            stock = ProductWarehouseStock.builder()
                    .product(product)
                    .warehouse(warehouse)
                    .reservedQuantity(0)
                    .physicalQuantity(newQuantity)
                    .build();
        } else {
            stock.setPhysicalQuantity(newQuantity);
        }

        productWarehouseStockRepository.save(stock);

        WarehouseStockTransaction warehouseStockTransaction = warehouseStockTransactionRepository.save(
                WarehouseStockTransaction
                        .builder()
                        .product(product)
                        .warehouse(warehouse)
                        .oldQuantity(oldQuantity)
                        .newQuantity(newQuantity)
                        .delta(delta)
                        .purposeType(purposeType)
                        .user(user)
                        .build()
        );

        return warehouseStockTransactionRepository.findByIdForStockManagementResponse(warehouseStockTransaction.getId())
                .orElseThrow(() -> new EntityNotFoundException("transaction with id [" + warehouseStockTransaction.getId() + "] is not found"));
    }

}
