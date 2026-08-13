package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.customer.CustomerOrderStatusType;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link CustomerOrderStatusType.CustomerOrderStatusCode}.
 */
@Converter
public class CustomerOrderStatusCodeConverter extends AbstractEnumCodeAttributeConverter<CustomerOrderStatusType.CustomerOrderStatusCode> {

    public CustomerOrderStatusCodeConverter() {
        super(CustomerOrderStatusType.CustomerOrderStatusCode.class);
    }
}
