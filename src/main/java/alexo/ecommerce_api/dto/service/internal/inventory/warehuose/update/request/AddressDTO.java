package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import org.openapitools.jackson.nullable.JsonNullable;

public record AddressDTO(
        @NotBlank
        @Max(255)
        JsonNullable<String> address,

        @NotBlank
        @Max(20)
        JsonNullable<String> mailIndex,

        @NotBlank
        @Max(100)
        JsonNullable<String> country,

        @NotBlank
        @Max(100)
        JsonNullable<String> city
) {
}
