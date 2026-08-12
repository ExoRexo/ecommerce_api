package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WarehouseCreateRequestDTO(
        @NotNull
        @Valid
        AddressDTO address,

        @NotNull
        @Size(min = 5, max = 100)
        String name
) {
        @Schema(name = "WarehouseCreateRequestDTO.AddressDTO")
        public record AddressDTO(
                @NotNull
                @Size(min = 10, max = 255)
                String address,

                @NotNull
                @Size(max = 20)
                String mailIndex,

                @NotNull
                @Size(max = 100)
                String country,

                @NotNull
                @Size(max = 100)
                String city
        ) {
        }
}
