package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.customer.wallet.CustomerWalletTransactionPurposeType;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode}.
 */
@Converter
public class CustomerWalletTransactionPurposeCodeConverter
    extends AbstractEnumCodeAttributeConverter<CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode> {

    public CustomerWalletTransactionPurposeCodeConverter() {
        super(CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode.class);
    }
}
