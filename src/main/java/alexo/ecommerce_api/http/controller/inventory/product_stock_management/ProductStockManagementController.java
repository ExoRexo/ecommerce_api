package alexo.ecommerce_api.http.controller.inventory.product_stock_management;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.transaction_list.TransactionListRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.transaction_list.TransactionListResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.update.ProductStockUpdateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.update.ProductStockUpdateResponseDTO;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import alexo.ecommerce_api.service.internal.inventory.product_stock_management.ProductStockManagementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inventory/product-stock-management")
public class ProductStockManagementController {
    private final ProductStockManagementService productStockManagementService;

    @PutMapping("/update-warehouse-stock")
    //    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_INVENTORY_PRODUCT_STOCK_MANAGEMENT_UPDATE_WAREHOUSE_STOCK')") // todo
    public ProductStockUpdateResponseDTO updateProductStockOnWarehouse(@Valid @RequestBody ProductStockUpdateRequestDTO request) {
        return productStockManagementService.updateProductPhysicalStockOnWarehouse(request);
    }

    @GetMapping("/warehouse-transactions-list")
    //    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_INVENTORY_PRODUCT_STOCK_MANAGEMENT_READ_WH_TRANSACTIONS_LIST')") // todo
    public ResponseEntity<@NotNull ApiResponseDTO<PageResponseDTO<TransactionListResponseDTO>>> getWarehouseTransactionsList(
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size,
            @RequestParam(name = "transactionId", required = false) Long transactionId,
            @RequestParam(name = "oldQuantity", required = false) Integer oldQuantity,
            @RequestParam(name = "newQuantity", required = false) Integer newQuantity,
            @RequestParam(name = "delta", required = false) Integer delta,
            @RequestParam(name = "warehouseName", required = false) String warehouseName,
            @RequestParam(name = "warehouseId", required = false) Long warehouseId,
            @RequestParam(name = "purposeCode", required = false) WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode purposeCode,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "productId", required = false) Long productId,
            @RequestParam(name = "createdAt", required = false) OffsetDateTime createdAt
    ) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(productStockManagementService.getTransactionsList(
                        new TransactionListRequestDTO(
                                new TransactionListRequestDTO.SortDTO(
                                        sortField,
                                        sortDirection
                                ),
                                new TransactionListRequestDTO.PaginationDTO(
                                        page,
                                        size
                                ),
                                new TransactionListRequestDTO.FiltersDTO(
                                        transactionId,
                                        oldQuantity,
                                        newQuantity,
                                        delta,
                                        warehouseName,
                                        warehouseId,
                                        purposeCode,
                                        userId,
                                        productId,
                                        createdAt
                                )
                        )
                )));
    }
}
