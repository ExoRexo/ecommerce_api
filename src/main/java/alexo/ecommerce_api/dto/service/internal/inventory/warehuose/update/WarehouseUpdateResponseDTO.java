package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update;

import io.swagger.v3.oas.annotations.media.Schema;

public record WarehouseUpdateResponseDTO(
        Long id,
        String name,
        AddressDTO address
) {
    @Schema(name = "WarehouseUpdateResponseDTO.AddressDTO")
    public record AddressDTO(
            String address,
            String mailIndex,
            String country,
            String city
    ) {
    }
}
