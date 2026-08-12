package alexo.ecommerce_api.dto.service.internal.catalog.product.update.request;

import alexo.ecommerce_api.enums.entity.ProductStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;

public record ProductUpdateRequestDTO(
        @NotNull
        Long productId,

        @NotNull
        @Size(min = 5, max = 100)
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        JsonNullable<String> name,

        @NotNull
        @Size(min = 10, max = 2000)
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        JsonNullable<String> description,

        @NotNull
        @DecimalMin("0")
        @DecimalMax("1000000")
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        JsonNullable<BigDecimal> priceRub,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        JsonNullable<ProductStatusCode> statusCode,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        JsonNullable<Long> categoryId
) {
}

