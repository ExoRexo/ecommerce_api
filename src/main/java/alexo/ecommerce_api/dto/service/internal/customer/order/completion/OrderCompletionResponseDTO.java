package alexo.ecommerce_api.dto.service.internal.customer.order.completion;

import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationResponseDTO;
import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record OrderCompletionResponseDTO(
        CustomerOrderDTO order,
        List<OrderItemReservationResponseDTO> reservations
) {

    @Schema(name = "OrderCompletionResponseDTO.CustomerOrderDTO")
    public record CustomerOrderDTO(
            Long id,
            StatusTypeDTO status
    ){

        @Schema(name = "OrderCompletionResponseDTO.CustomerOrderDTO.StatusTypeDTO")
        public record StatusTypeDTO(
                String label,
                String description,
                CustomerOrderStatusType.CustomerOrderStatusCode code
        ) {

        }

    }
}
