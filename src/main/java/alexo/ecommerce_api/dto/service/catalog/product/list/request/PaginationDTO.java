package alexo.ecommerce_api.dto.service.catalog.product.list.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record PaginationDTO(
        @NotNull
        Integer page,

        @NotNull
        @Max(50)
        Integer size
) {
}
