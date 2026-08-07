package alexo.ecommerce_api.http.controller.auth;

import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.service.identity.auth.login.LoginService;
import alexo.ecommerce_api.service.identity.auth.login.dto.AuthLoginRequestDTO;
import alexo.ecommerce_api.service.identity.auth.login.dto.AuthTokenResponseDTO;
import alexo.ecommerce_api.service.identity.auth.signup.SignupService;
import alexo.ecommerce_api.service.identity.auth.signup.dto.request.UserCreationRequestDTO;
import alexo.ecommerce_api.service.identity.auth.signup.dto.response.StatusTypeDTO;
import alexo.ecommerce_api.service.identity.auth.signup.dto.response.UserCreationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication API endpoints.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;

    /**
     * Registers a new user account.
     *
     * @param request sign-up request payload
     * @return unified response with created user payload
     */
    @PostMapping("/signup")
    public UserCreationResponseDTO signup(@Valid @RequestBody UserCreationRequestDTO request) {
        User user = signupService.createUser(request, null, null);

        return new UserCreationResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                new StatusTypeDTO(
                        user.getStatusType().getCode(),
                        user.getStatusType().getLabel(),
                        user.getStatusType().getDescription()
                )
        );
    }

    /**
     * Authenticates user and returns JWT token payload.
     *
     * @param request login request payload
     * @return unified response with token payload
     */
    @PostMapping("/login")
    public AuthTokenResponseDTO login(@Valid @RequestBody AuthLoginRequestDTO request) {
        return loginService.authenticateUser(request);
    }

    /**
     * Returns new JWT token payload. Authentication required for this method
     */
    @PostMapping("/refresh")
    public AuthTokenResponseDTO refresh() {
        return loginService.refresh();
    }
}
