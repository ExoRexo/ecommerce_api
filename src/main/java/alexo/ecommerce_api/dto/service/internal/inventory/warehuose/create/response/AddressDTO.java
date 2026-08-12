package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.response;

public record AddressDTO(
        String address,
        String mailIndex,
        String country,
        String city
) {
}
