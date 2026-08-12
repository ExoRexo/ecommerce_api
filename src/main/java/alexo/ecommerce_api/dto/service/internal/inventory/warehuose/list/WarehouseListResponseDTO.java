package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.list;

import io.swagger.v3.oas.annotations.media.Schema;

public record WarehouseListResponseDTO(
        Long id,
        String name,
        AddressDTO address
) {
    @Schema(name = "WarehouseListResponseDTO.AddressDTO")
    public record AddressDTO(
            String address,
            String mailIndex,
            String country,
            String city
    ) {
    }
}
