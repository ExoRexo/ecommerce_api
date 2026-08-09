package alexo.ecommerce_api.dto.configuration.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.jwt")
public record JwtPropertiesDTO(
        @NotBlank String secret,
        @Positive long expirationMs
) {
}
