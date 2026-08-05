package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Wallet transaction intents stored in c_wallt_transaction_purpose_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum CustomerWalletTransactionPurposeCode implements EnumCode {
    WITHDRAWAL("WITHDRAWAL"),
    TOP_UP("TOP-UP");

    private final String code;
}
