package alexo.ecommerce_api.dto.service.internal.identity.authentication.login;

import java.time.Instant;

public record AuthTokenResponseDTO(
        String accessToken,
        String tokenType,
        Instant expiresAt
) {
}
