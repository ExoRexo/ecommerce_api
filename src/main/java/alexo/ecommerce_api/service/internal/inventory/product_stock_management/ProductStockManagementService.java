package alexo.ecommerce_api.service.internal.inventory.product_stock_management;

import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.ProductStockUpdateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.ProductStockUpdateResponseDTO;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransaction;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
@Valid
public class ProductStockManagementService {
    private final TransactionalProductStockService transactionalProductStockService;

    public ProductStockUpdateResponseDTO updateProductStockOnWarehouse(@Valid ProductStockUpdateRequestDTO request) {
        Assert.notNull(request, "request must be not null");

        WarehouseStockTransaction warehouseStockTransaction = transactionalProductStockService.updateProductStockOnWarehouse(request);

        return new ProductStockUpdateResponseDTO(
                warehouseStockTransaction.getId(),
                warehouseStockTransaction.getOldQuantity(),
                warehouseStockTransaction.getNewQuantity(),
                warehouseStockTransaction.getDelta(),
                new ProductStockUpdateResponseDTO.WarehouseDTO(
                        warehouseStockTransaction.getWarehouse().getId(),
                        warehouseStockTransaction.getWarehouse().getName(),

                        new ProductStockUpdateResponseDTO.WarehouseDTO.WarehouseAddressDTO(
                                warehouseStockTransaction.getWarehouse().getAddress().getAddress(),
                                warehouseStockTransaction.getWarehouse().getAddress().getMailIndex(),
                                warehouseStockTransaction.getWarehouse().getAddress().getCountry(),
                                warehouseStockTransaction.getWarehouse().getAddress().getCity()
                        )
                ),

                new ProductStockUpdateResponseDTO.PurposeTypeDTO(
                        warehouseStockTransaction.getPurposeType().getLabel(),
                        warehouseStockTransaction.getPurposeType().getDescription(),
                        warehouseStockTransaction.getPurposeType().getCode()
                ),

                new ProductStockUpdateResponseDTO.UserDTO(
                        warehouseStockTransaction.getUser().getId(),
                        warehouseStockTransaction.getUser().getFirstName(),
                        warehouseStockTransaction.getUser().getLastName()
                ),

                new ProductStockUpdateResponseDTO.ProductDTO(
                        warehouseStockTransaction.getProduct().getId(),
                        warehouseStockTransaction.getProduct().getName()
                ),

                warehouseStockTransaction.getCreatedAt()
        );
    }

}
