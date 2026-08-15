package alexo.ecommerce_api.mapper.customer.order.creation;

import alexo.ecommerce_api.dto.service.internal.customer.order.creation.OrderCreationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationResponseDTO;
import alexo.ecommerce_api.entity.customer.order.CustomerOrder;
import org.springframework.util.Assert;

import java.util.List;

public class OrderCreationMapper {
    public static OrderCreationResponseDTO fromOrder(CustomerOrder order, List<OrderItemReservationResponseDTO> orderItemReservationResponseDTOS) {
        Assert.notNull(order, "order must be not null");
        Assert.notNull(orderItemReservationResponseDTOS, "orderItemReservationResponseDTOS must be not null");

        return new OrderCreationResponseDTO(
                new OrderCreationResponseDTO.CustomerOrderDTO(
                        order.getId(),
                        new OrderCreationResponseDTO.CustomerOrderDTO.StatusTypeDTO(
                                order.getStatusType().getLabel(),
                                order.getStatusType().getDescription(),
                                order.getStatusType().getCode()
                        )
                ),
                orderItemReservationResponseDTOS
        );
    }
}
