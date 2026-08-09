package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.enums.entity.OrderItemReservationStatusCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link OrderItemReservationStatusCode}.
 */
@Converter
public class OrderItemReservationStatusCodeConverter
    extends AbstractEnumCodeAttributeConverter<OrderItemReservationStatusCode> {

    public OrderItemReservationStatusCodeConverter() {
        super(OrderItemReservationStatusCode.class);
    }
}
