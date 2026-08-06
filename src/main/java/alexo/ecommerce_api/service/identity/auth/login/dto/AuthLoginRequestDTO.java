package alexo.ecommerce_api.service.identity.auth.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
