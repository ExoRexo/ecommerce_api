package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update.response;

public record WarehouseUpdateResponseDTO(
        Long id,
        String name,
        AddressDTO address
) {
}
