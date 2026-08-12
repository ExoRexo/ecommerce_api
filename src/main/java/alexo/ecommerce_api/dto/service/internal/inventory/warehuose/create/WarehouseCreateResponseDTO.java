package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create;

import io.swagger.v3.oas.annotations.media.Schema;

public record WarehouseCreateResponseDTO(
        Long id,
        String name,
        AddressDTO address
) {
    @Schema(name = "WarehouseCreateResponseDTO.AddressDTO")
    public record AddressDTO(
            String address,
            String mailIndex,
            String country,
            String city
    ) {
    }
}
