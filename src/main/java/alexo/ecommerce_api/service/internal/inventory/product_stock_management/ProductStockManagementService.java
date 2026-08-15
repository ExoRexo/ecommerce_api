package alexo.ecommerce_api.service.internal.inventory.product_stock_management;

import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.stock_list.StockListRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.stock_list.StockListResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.transaction_list.TransactionListRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.transaction_list.TransactionListResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update.ProductStockUpdateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update.ProductStockUpdateResponseDTO;
import alexo.ecommerce_api.entity.inventory.ProductWarehouseStock;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransaction;
import alexo.ecommerce_api.mapper.inventory.product_stock_management.ProductStockManagementMapper;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseStockTransactionRepository;
import alexo.ecommerce_api.specification.inventory.product_stock_management.ProductStockManagementSpecifications;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class ProductStockManagementService {
    private final TransactionalProductStockService transactionalProductStockService;
    private final WarehouseStockTransactionRepository warehouseStockTransactionRepository;
    private final ProductWarehouseStockRepository productWarehouseStockRepository;

    public ProductStockUpdateResponseDTO updateProductPhysicalStockOnWarehouse(@Valid ProductStockUpdateRequestDTO request) {
        Assert.notNull(request, "request must be not null");

        return transactionalProductStockService.updateProductPhysicalStockOnWarehouse(request);
    }

    public PageResponseDTO<StockListResponseDTO> getProductStockList(@Valid StockListRequestDTO request)  {
        Assert.notNull(request, "request must be not null");

        List<Sort.Order> orders = new ArrayList<>();

        orders.add(new Sort.Order(request.sortDTO().direction(), request.sortDTO().field()));

        PageRequest pageRequest = PageRequest.of(
                request.paginationDTO().page(),
                request.paginationDTO().size(),
                Sort.by(orders)
        );

        Specification<@NotNull ProductWarehouseStock> specification = ProductStockManagementSpecifications.ProductStockListSpecification.getSpecification(request.filtersDTO());

        Page<@NotNull ProductWarehouseStock> warehouseStockTransactionsPage = productWarehouseStockRepository.findAll(specification, pageRequest);

        return PageResponseDTO.from(warehouseStockTransactionsPage.map(ProductStockManagementMapper::fromProductWarehouseStockToStockListResponseDTO));
    }

    public PageResponseDTO<TransactionListResponseDTO> getTransactionsList(@Valid TransactionListRequestDTO request)  {
        Assert.notNull(request, "request must be not null");

        List<Sort.Order> orders = new ArrayList<>();

        orders.add(new Sort.Order(request.sortDTO().direction(), request.sortDTO().field()));

        PageRequest pageRequest = PageRequest.of(
                request.paginationDTO().page(),
                request.paginationDTO().size(),
                Sort.by(orders)
        );

        Specification<@NotNull WarehouseStockTransaction> specification = ProductStockManagementSpecifications.TransactionListSpecification.getSpecification(request.filtersDTO());

        Page<@NotNull WarehouseStockTransaction> warehouseStockTransactionsPage = warehouseStockTransactionRepository.findAll(specification, pageRequest);

        return PageResponseDTO.from(warehouseStockTransactionsPage.map(ProductStockManagementMapper::fromWarehouseStockTransactionToTransactionListResponseDTO));
    }

}
