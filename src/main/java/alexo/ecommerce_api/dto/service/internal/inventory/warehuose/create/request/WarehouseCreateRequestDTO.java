package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WarehouseCreateRequestDTO(
        @NotNull
        AddressDTO address,

        @NotBlank
        @Size(min = 5, max = 100)
        String name
) {
}
