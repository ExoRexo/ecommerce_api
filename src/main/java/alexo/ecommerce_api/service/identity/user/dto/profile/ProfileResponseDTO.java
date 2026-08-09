package alexo.ecommerce_api.service.identity.user.dto.profile;

public record ProfileResponseDTO(
        String email,
        String firstName,
        String lastName,
        StatusTypeDTO status
) {}
