package alexo.ecommerce_api.service.identity.auth.login;

import alexo.ecommerce_api.configuration.security.jwt.JwtService;
import alexo.ecommerce_api.service.identity.auth.login.dto.AuthLoginRequestDTO;
import alexo.ecommerce_api.service.identity.auth.login.dto.AuthTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserPrincipalService userPrincipalService;

    public AuthTokenResponseDTO login(AuthLoginRequestDTO request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserPrincipal principal = (UserPrincipal) userPrincipalService.loadUserByUsername(request.email());
        String accessToken = jwtService.generateToken(principal);

        return new AuthTokenResponseDTO(
                accessToken,
                "Bearer",
                Instant.now().plusMillis(jwtService.getExpirationMs())
        );
    }
}
