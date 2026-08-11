package alexo.ecommerce_api.dto.service.catalog.product.list.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record SortDTO(
        @NotNull String field,
        @NotNull Sort.Direction direction
) {
}
