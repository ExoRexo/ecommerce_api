package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.common.EnumCodeAttributeConverter;
import alexo.ecommerce_api.entity.enums.CustomerOrderStatusCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link CustomerOrderStatusCode}.
 */
@Converter
public class CustomerOrderStatusCodeConverter extends EnumCodeAttributeConverter<CustomerOrderStatusCode> {

    public CustomerOrderStatusCodeConverter() {
        super(CustomerOrderStatusCode.class);
    }
}
