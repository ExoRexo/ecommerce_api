package alexo.ecommerce_api.dto.service.catalog.product.create.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductCreateRequestDTO(
        @NotNull
        @Size(min = 5, max = 100)
        String name,

        @NotNull
        @Size(min = 10, max = 2000)
        String description,

        @NotNull
        @DecimalMin("0")
        @DecimalMax("1000000")
        BigDecimal priceRub,

        @NotNull
        Long categoryId
) {
}

