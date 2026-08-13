package alexo.ecommerce_api.http.controller.inventory.product_stock_management;

import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.ProductStockUpdateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.ProductStockUpdateResponseDTO;
import alexo.ecommerce_api.service.internal.inventory.product_stock_management.ProductStockManagementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inventory/product-stock-management")
public class ProductStockManagementController {
    private final ProductStockManagementService productStockManagementService;

    @PutMapping("/update-warehouse-stock")
    //    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_INVENTORY_PRODUCT_STOCK_MANAGEMENT_UPDATE_WAREHOUSE_STOCK')") // todo
    public ProductStockUpdateResponseDTO updateProductStockOnWarehouse(@Valid @RequestBody ProductStockUpdateRequestDTO request) {
        return productStockManagementService.updateProductStockOnWarehouse(request);
    }
}
