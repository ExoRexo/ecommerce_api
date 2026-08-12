package alexo.ecommerce_api.dto.service.internal.catalog.category.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public record CategoryUpdateRequestDTO(
        @NotNull
        Long categoryId,

        @Size(min = 10, max = 100)
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        JsonNullable<String> name,

        JsonNullable<Long> parentId
) {
}
