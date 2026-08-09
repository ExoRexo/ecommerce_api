package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.enums.entity.CustomerOrderStatusCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link CustomerOrderStatusCode}.
 */
@Converter
public class CustomerOrderStatusCodeConverter extends AbstractEnumCodeAttributeConverter<CustomerOrderStatusCode> {

    public CustomerOrderStatusCodeConverter() {
        super(CustomerOrderStatusCode.class);
    }
}
