package alexo.ecommerce_api.enums.entity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import alexo.ecommerce_api.contract.enums.EnumDescription;
import alexo.ecommerce_api.contract.enums.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Warehouse stock transaction intents stored in wh_st_transaction_purpose_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum WarehouseStockTransactionPurposeCode implements EnumCode, EnumLabel, EnumDescription {
    SALE("Продажа", "Остаток уменьшен в результате продажи клиенту."),
    PURCHASE("Закупка", "Остаток увеличен в результате закупки."),
    INVENTORY_ADJUSTMENT("Инвентаризационная корректировка", "Остаток скорректирован вручную по итогам инвентаризации.");

    private final String label;
    private final String description;
}
