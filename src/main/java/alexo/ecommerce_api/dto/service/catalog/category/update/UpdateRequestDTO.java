package alexo.ecommerce_api.dto.service.catalog.category.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateRequestDTO(
        @NotNull
        Long categoryId,

        @Size(min = 10, max = 100)
        String name,

        JsonNullable<Long> parentId
) {
}
