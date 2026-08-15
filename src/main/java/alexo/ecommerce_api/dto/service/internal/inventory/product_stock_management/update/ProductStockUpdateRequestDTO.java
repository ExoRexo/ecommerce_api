package alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import alexo.ecommerce_api.validation.numeric.NotZero;
import jakarta.validation.constraints.NotNull;

public record ProductStockUpdateRequestDTO(
        @NotNull Long productId,
        @NotNull Long warehouseId,

        @NotNull
        @NotZero
        Integer deltaQuantity,

        @NotNull WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode purposeCode
) {
}
