package alexo.ecommerce_api.dto.service.internal.customer.order.details;

import jakarta.validation.constraints.NotNull;

public record OrderDetailsRequestDTO(
        @NotNull
        Long orderId
) {
}
