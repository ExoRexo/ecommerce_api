package alexo.ecommerce_api.service.identity.auth.login;

import alexo.ecommerce_api.configuration.security.UserPrincipalService;
import alexo.ecommerce_api.configuration.security.jwt.JwtService;
import alexo.ecommerce_api.service.identity.auth.login.dto.AuthLoginRequestDTO;
import alexo.ecommerce_api.service.identity.auth.login.dto.AuthTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserPrincipalService userPrincipalService;

    public AuthTokenResponseDTO authenticateUser(AuthLoginRequestDTO request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return generateTokenForPrincipal((UserPrincipal) userPrincipalService.loadUserByUsername(request.email()));
    }

    public AuthTokenResponseDTO refresh() {
        UserPrincipal userPrincipal = Objects.requireNonNull(getCurrentUserPrincipal());

        Long userId = Objects.requireNonNull(userPrincipal.getId());

        return generateTokenForPrincipal(getFreshPrincipalForUserId(userId));
    }

    private UserPrincipal getFreshPrincipalForUserId(@NotNull Long userId) {
        return (UserPrincipal) userPrincipalService.loadUserById(userId);
    }

    private UserPrincipal getCurrentUserPrincipal() {
        return (UserPrincipal) Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                        .getPrincipal();
    }

    private AuthTokenResponseDTO generateTokenForPrincipal(UserPrincipal principal) {
        String accessToken = jwtService.generateToken(principal);

        return new AuthTokenResponseDTO(
                accessToken,
                "Bearer",
                Instant.now().plusMillis(jwtService.getExpirationMs())
        );
    }
}
