package alexo.ecommerce_api.mapper.customer.order.cancellation;

import alexo.ecommerce_api.dto.service.internal.customer.order.cancellation.OrderCancellationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationResponseDTO;
import alexo.ecommerce_api.entity.customer.order.CustomerOrder;
import org.springframework.util.Assert;

import java.util.List;

public class OrderCancellationMapper {
    public static OrderCancellationResponseDTO fromOrder(CustomerOrder order, List<OrderItemReservationResponseDTO> orderItemReservationResponseDTOS) {
        Assert.notNull(order, "order must be not null");
        Assert.notNull(orderItemReservationResponseDTOS, "orderItemReservationResponseDTOS must be not null");

        return new OrderCancellationResponseDTO(
                new OrderCancellationResponseDTO.CustomerOrderDTO(
                        order.getId(),
                        new OrderCancellationResponseDTO.CustomerOrderDTO.StatusTypeDTO(
                                order.getStatusType().getLabel(),
                                order.getStatusType().getDescription(),
                                order.getStatusType().getCode()
                        )
                ),
                orderItemReservationResponseDTOS
        );
    }
}
