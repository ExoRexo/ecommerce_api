package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.customer.order.OrderItemReservationStatusType;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link OrderItemReservationStatusType.OrderItemReservationStatusCode}.
 */
@Converter
public class OrderItemReservationStatusCodeConverter
    extends AbstractEnumCodeAttributeConverter<OrderItemReservationStatusType.OrderItemReservationStatusCode> {

    public OrderItemReservationStatusCodeConverter() {
        super(OrderItemReservationStatusType.OrderItemReservationStatusCode.class);
    }
}
