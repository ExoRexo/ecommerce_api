package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
        @NotBlank
        @Max(255)
        String address,

        @NotBlank
        @Max(20)
        String mailIndex,

        @NotBlank
        @Max(100)
        String country,

        @NotBlank
        @Max(100)
        String city
) {
}
