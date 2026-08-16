package alexo.ecommerce_api.dto.service.internal.customer.order.list;

import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record OrderListResponseDTO(
        Long orderId,
        OffsetDateTime createdAt,
        StatusTypeDTO status
) {

    @Schema(name = "OrderListResponseDTO.StatusTypeDTO")
    public record StatusTypeDTO(
            String label,
            String description,
            CustomerOrderStatusType.CustomerOrderStatusCode code
    ) {
    }
}


