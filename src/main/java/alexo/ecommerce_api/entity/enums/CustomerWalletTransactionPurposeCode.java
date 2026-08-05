package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Wallet transaction intents stored in c_wallt_transaction_purpose_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum CustomerWalletTransactionPurposeCode implements EnumCode, EnumLabel, EnumDescription {
    WITHDRAWAL("WITHDRAWAL", "Withdrawal", "Funds are withdrawn from the customer wallet."),
    TOP_UP("TOP-UP", "Top-up", "Funds are added to the customer wallet.");

    private final String code;
    private final String label;
    private final String description;
}
