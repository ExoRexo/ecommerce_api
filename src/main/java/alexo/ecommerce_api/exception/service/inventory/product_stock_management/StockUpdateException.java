package alexo.ecommerce_api.exception.service.inventory.product_stock_management;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import jakarta.validation.constraints.NotNull;

public class StockUpdateException extends RuntimeException {
    public StockUpdateException(String message) {
        super(message);
    }

    public static StockUpdateException stockWasNotInitializedAndPassedDeltaIsLessThan0(Long productId, Long warehouseId) {
        return new StockUpdateException("stock was not initialized for productId[" + productId + "] on warehouse[" + warehouseId + "], and passed stock deltaQuantity is < 0");
    }

    public static StockUpdateException stockDecreaseIsGreaterThanCurrentPhysicalQuantity(Integer deltaQuantity, Integer physicalQuantity, Long productId, Long warehouseId) {
        return new StockUpdateException("stock decrease [" + deltaQuantity + "] is greater than current physical quantity[" + physicalQuantity + "] productId[" + productId + "] on warehouse[" + warehouseId + "]");
    }


    public static StockUpdateException stockDecreaseResultIsLessThanCurrentReserves(Integer decreaseResult, Integer deltaQuantity, Integer physicalQuantity, Long productId, Long warehouseId) {
        return new StockUpdateException("stock decrease result[" + decreaseResult + "] with delta[" + deltaQuantity + "] is greater than current reserves[" + physicalQuantity + "] productId[" + productId + "] on warehouse[" + warehouseId + "]");
    }

    public static StockUpdateException operationIsNotAllowedForThisPurpose(WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode purposeCode,
                                                                           WarehouseStockTransactionPurposeType.WarehouseStockTransactionOperationCode operationCode,
                                                                           @NotNull Long productId,
                                                                           @NotNull Long warehouseId
    ) {
        return new StockUpdateException("operation[" + operationCode + "] is not allowed for this purpose[" + purposeCode + "], productId[" + productId + "] on warehouse[" + warehouseId + "]");
    }
}
