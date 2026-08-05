package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Warehouse stock transaction intents stored in wh_st_transaction_purpose_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum WarehouseStockTransactionPurposeCode implements EnumCode, EnumLabel, EnumDescription {
    SALE("SALE", "Sale", "Stock was decreased due to customer sale."),
    PURCHASE("PURCHASE", "Purchase", "Stock was increased due to procurement."),
    INVENTORY_ADJUSTMENT("INVENTORY_ADJUSTMENT", "Inventory Adjustment", "Stock was corrected by manual inventory adjustment.");

    private final String code;
    private final String label;
    private final String description;
}
