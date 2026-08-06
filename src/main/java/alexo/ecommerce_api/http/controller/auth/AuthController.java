package alexo.ecommerce_api.http.controller.auth;

import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.service.identity.auth.login.LoginService;
import alexo.ecommerce_api.service.identity.auth.login.dto.AuthLoginRequestDTO;
import alexo.ecommerce_api.service.identity.auth.login.dto.AuthTokenResponseDTO;
import alexo.ecommerce_api.service.identity.auth.signup.SignupService;
import alexo.ecommerce_api.service.identity.auth.signup.dto.UserCreationRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;

    @PostMapping("/signup")
    public User signup(@Valid @RequestBody UserCreationRequestDTO request) {
        return signupService.createUser(request, null, null);
    }

    @PostMapping("/login")
    public AuthTokenResponseDTO login(@Valid @RequestBody AuthLoginRequestDTO request) {
        return loginService.authenticateUser(request);
    }
}
