package alexo.ecommerce_api.dto.service.internal.customer.order.completion;

import jakarta.validation.constraints.NotNull;

public record OrderCompletionRequestDTO(
        @NotNull
        Long orderId
) {}
