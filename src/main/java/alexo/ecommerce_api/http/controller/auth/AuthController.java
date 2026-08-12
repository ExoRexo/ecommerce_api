package alexo.ecommerce_api.http.controller.auth;

import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.service.internal.identity.authentication.login.LoginService;
import alexo.ecommerce_api.dto.service.identity.authentication.login.AuthLoginRequestDTO;
import alexo.ecommerce_api.dto.service.identity.authentication.login.AuthTokenResponseDTO;
import alexo.ecommerce_api.service.internal.identity.authentication.signup.SignupService;
import alexo.ecommerce_api.dto.service.identity.authentication.signup.request.UserSignupRequestDTO;
import alexo.ecommerce_api.dto.service.identity.authentication.signup.response.StatusTypeDTO;
import alexo.ecommerce_api.dto.service.identity.authentication.signup.response.UserSignupResponseDTO;
import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<@NotNull ApiResponseDTO<UserSignupResponseDTO>> signup(@Valid @RequestBody UserSignupRequestDTO request) {
        User user = signupService.createUser(request, null, null);

        UserSignupResponseDTO response = new UserSignupResponseDTO(
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

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(response));
    }

    /**
     * Authenticates user and returns JWT token payload.
     *
     * @param request login request payload
     * @return unified response with token payload
     */
    @PostMapping("/login")
    public ResponseEntity<@NotNull ApiResponseDTO<AuthTokenResponseDTO>> login(@Valid @RequestBody AuthLoginRequestDTO request) {
        return ResponseEntity.ok(ApiResponseDTO.success(loginService.authenticateUser(request)));
    }

    /**
     * Returns new JWT token payload. Authentication required for this method
     */
    @PostMapping("/refresh")
    public ResponseEntity<@NotNull ApiResponseDTO<AuthTokenResponseDTO>> refresh() {
        return ResponseEntity.ok(ApiResponseDTO.success(loginService.refresh()));
    }
}
