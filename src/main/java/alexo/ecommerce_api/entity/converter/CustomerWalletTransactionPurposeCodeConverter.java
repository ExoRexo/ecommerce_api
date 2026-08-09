package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.enums.entity.CustomerWalletTransactionPurposeCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link CustomerWalletTransactionPurposeCode}.
 */
@Converter
public class CustomerWalletTransactionPurposeCodeConverter
    extends AbstractEnumCodeAttributeConverter<CustomerWalletTransactionPurposeCode> {

    public CustomerWalletTransactionPurposeCodeConverter() {
        super(CustomerWalletTransactionPurposeCode.class);
    }
}
