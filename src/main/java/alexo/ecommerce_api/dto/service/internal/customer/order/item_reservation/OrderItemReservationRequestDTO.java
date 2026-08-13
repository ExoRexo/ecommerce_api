package alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemReservationRequestDTO(
        @NotNull
        Long orderItemId,

        @NotNull
        @Min(1)
        Integer quantityToReserve,

        @NotNull
        Long warehouseId
) {
}
