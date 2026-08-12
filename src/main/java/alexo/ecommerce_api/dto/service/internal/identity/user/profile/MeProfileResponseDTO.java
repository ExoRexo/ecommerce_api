package alexo.ecommerce_api.dto.service.internal.identity.user.profile;

public record MeProfileResponseDTO(
        String email,
        String firstName,
        String lastName,
        StatusTypeDTO status
) {}
