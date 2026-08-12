package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public record WarehouseUpdateRequestDTO(
        @NotNull
        Long warehouseId,

        @NotNull
        JsonNullable<AddressDTO> address,

        @NotBlank
        @Size(min = 5, max = 100)
        JsonNullable<String> name
) {
}
