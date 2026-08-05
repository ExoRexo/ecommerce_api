package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.common.EnumCodeAttributeConverter;
import alexo.ecommerce_api.entity.enums.OrderItemReservationStatusCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link OrderItemReservationStatusCode}.
 */
@Converter
public class OrderItemReservationStatusCodeConverter
    extends EnumCodeAttributeConverter<OrderItemReservationStatusCode> {

    public OrderItemReservationStatusCodeConverter() {
        super(OrderItemReservationStatusCode.class);
    }
}
