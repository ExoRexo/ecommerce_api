package alexo.ecommerce_api.service.identity.authentication.signup.dto.response;

import java.time.OffsetDateTime;

public record UserCreationResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        OffsetDateTime createdAt,
        StatusTypeDTO statusType
) {
}
