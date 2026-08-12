package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public record WarehouseUpdateRequestDTO(
        @NotNull
        Long warehouseId,

        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Valid
        JsonNullable<AddressDTO> address,

        @NotBlank
        @Size(min = 5, max = 100)
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        JsonNullable<String> name
) {
        @Schema(name = "WarehouseUpdateRequestDTO.AddressDTO")
        public record AddressDTO(
                @NotBlank
                @Size(max = 255)
                @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                JsonNullable<String> address,

                @NotBlank
                @Size(max = 20)
                @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                JsonNullable<String> mailIndex,

                @NotBlank
                @Size(max = 100)
                @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                JsonNullable<String> country,

                @NotBlank
                @Size(max = 100)
                @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                JsonNullable<String> city
        ) {
        }
}
