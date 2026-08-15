package alexo.ecommerce_api.mapper.customer.order.completion;

import alexo.ecommerce_api.dto.service.internal.customer.order.completion.OrderCompletionResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationResponseDTO;
import alexo.ecommerce_api.entity.customer.order.CustomerOrder;
import org.springframework.util.Assert;

import java.util.List;

public class OrderCompletionMapper {
    public static OrderCompletionResponseDTO fromOrder(CustomerOrder order, List<OrderItemReservationResponseDTO> orderItemReservationResponseDTOS) {
        Assert.notNull(order, "order must be not null");
        Assert.notNull(orderItemReservationResponseDTOS, "orderItemReservationResponseDTOS must be not null");

        return new OrderCompletionResponseDTO(
                new OrderCompletionResponseDTO.CustomerOrderDTO(
                        order.getId(),
                        new OrderCompletionResponseDTO.CustomerOrderDTO.StatusTypeDTO(
                                order.getStatusType().getLabel(),
                                order.getStatusType().getDescription(),
                                order.getStatusType().getCode()
                        )
                ),
                orderItemReservationResponseDTOS
        );
    }
}
