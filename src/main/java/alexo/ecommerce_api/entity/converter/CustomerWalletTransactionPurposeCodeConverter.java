package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.common.EnumCodeAttributeConverter;
import alexo.ecommerce_api.entity.enums.CustomerWalletTransactionPurposeCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link CustomerWalletTransactionPurposeCode}.
 */
@Converter
public class CustomerWalletTransactionPurposeCodeConverter
    extends EnumCodeAttributeConverter<CustomerWalletTransactionPurposeCode> {

    public CustomerWalletTransactionPurposeCodeConverter() {
        super(CustomerWalletTransactionPurposeCode.class);
    }
}
