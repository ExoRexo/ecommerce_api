package alexo.ecommerce_api.dto.service.internal.customer.order.cancellation;

import jakarta.validation.constraints.NotNull;

public record OrderCancellationRequestDTO(
        @NotNull
        Long orderId
) {}
