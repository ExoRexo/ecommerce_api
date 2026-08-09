package alexo.ecommerce_api.dto.service.identity.authentication.signup.response;

import java.time.OffsetDateTime;

public record UserSignupResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        OffsetDateTime createdAt,
        StatusTypeDTO statusType
) {
}
