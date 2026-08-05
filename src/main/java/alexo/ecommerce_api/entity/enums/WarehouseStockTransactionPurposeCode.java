package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Warehouse stock transaction intents stored in wh_st_transaction_purpose_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum WarehouseStockTransactionPurposeCode implements EnumCode {
    SALE("SALE"),
    PURCHASE("PURCHASE"),
    INVENTORY_ADJUSTMENT("INVENTORY_ADJUSTMENT");

    private final String code;
}
