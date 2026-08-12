package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.response;

public record WarehouseCreateResponseDTO(
        Long id,
        String name,
        AddressDTO address
) {
}
