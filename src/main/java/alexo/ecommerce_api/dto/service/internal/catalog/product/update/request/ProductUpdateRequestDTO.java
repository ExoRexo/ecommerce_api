package alexo.ecommerce_api.dto.service.internal.catalog.product.update.request;

import alexo.ecommerce_api.enums.entity.ProductStatusCode;
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
        JsonNullable<String> name,

        @NotNull
        @Size(min = 10, max = 2000)
        JsonNullable<String> description,

        @NotNull
        @DecimalMin("0")
        @DecimalMax("1000000")
        JsonNullable<BigDecimal> priceRub,

        @NotNull
        JsonNullable<ProductStatusCode> statusCode,

        @NotNull
        JsonNullable<Long> categoryId
) {
}

