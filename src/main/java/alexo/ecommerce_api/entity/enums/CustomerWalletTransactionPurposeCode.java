package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.enums.contract.EnumCode;
import alexo.ecommerce_api.entity.enums.contract.EnumDescription;
import alexo.ecommerce_api.entity.enums.contract.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Wallet transaction intents stored in c_wallt_transaction_purpose_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum CustomerWalletTransactionPurposeCode implements EnumCode, EnumLabel, EnumDescription {
    WITHDRAWAL("Списание", "Средства списываются с кошелька клиента."),
    TOP_UP("Пополнение", "Средства зачисляются на кошелек клиента.");

    private final String label;
    private final String description;
}
