package alexo.ecommerce_api.service.identity.authentication.login.dto;

import java.time.Instant;

public record AuthTokenResponseDTO(
        String accessToken,
        String tokenType,
        Instant expiresAt
) {
}
