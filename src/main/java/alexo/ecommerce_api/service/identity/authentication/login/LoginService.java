package alexo.ecommerce_api.service.identity.authentication.login;

import alexo.ecommerce_api.service.identity.authority.UserPrincipalService;
import alexo.ecommerce_api.service.configuration.security.jwt.JwtService;
import alexo.ecommerce_api.dto.service.identity.UserPrincipalDTO;
import alexo.ecommerce_api.dto.service.identity.authentication.login.AuthLoginRequestDTO;
import alexo.ecommerce_api.dto.service.identity.authentication.login.AuthTokenResponseDTO;
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

        return generateTokenForPrincipal((UserPrincipalDTO) userPrincipalService.loadUserByUsername(request.email()));
    }

    public AuthTokenResponseDTO refresh() {
        UserPrincipalDTO userPrincipalDTO = Objects.requireNonNull(getCurrentUserPrincipal());

        Long userId = Objects.requireNonNull(userPrincipalDTO.getId());

        return generateTokenForPrincipal(getFreshPrincipalForUserId(userId));
    }

    private UserPrincipalDTO getFreshPrincipalForUserId(@NotNull Long userId) {
        return (UserPrincipalDTO) userPrincipalService.loadUserById(userId);
    }

    private UserPrincipalDTO getCurrentUserPrincipal() {
        return (UserPrincipalDTO) Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                        .getPrincipal();
    }

    private AuthTokenResponseDTO generateTokenForPrincipal(UserPrincipalDTO principal) {
        String accessToken = jwtService.generateToken(principal);

        return new AuthTokenResponseDTO(
                accessToken,
                "Bearer",
                Instant.now().plusMillis(jwtService.getExpirationMs())
        );
    }
}
