package alexo.ecommerce_api.dto.service.identity.user.profile;

public record MeProfileResponseDTO(
        String email,
        String firstName,
        String lastName,
        StatusTypeDTO status
) {}
