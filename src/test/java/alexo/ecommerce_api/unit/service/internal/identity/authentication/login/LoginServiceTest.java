package alexo.ecommerce_api.unit.service.internal.identity.authentication.login;

import alexo.ecommerce_api.dto.service.internal.identity.UserPrincipalDTO;
import alexo.ecommerce_api.dto.service.internal.identity.authentication.login.AuthLoginRequestDTO;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.service.internal.identity.authentication.login.LoginService;
import alexo.ecommerce_api.service.internal.identity.authority.UserPrincipalService;
import alexo.ecommerce_api.service.internal.jwt.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserPrincipalService userPrincipalService;

    @InjectMocks
    private LoginService loginService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateAndGenerateToken() {
        UserPrincipalDTO principal = principal();
        when(userPrincipalService.loadUserByUsername("customer@example.com")).thenReturn(principal);
        when(jwtService.generateToken(principal)).thenReturn("token");
        when(jwtService.getExpirationMs()).thenReturn(3_600_000L);

        var response = loginService.authenticateUser(
                new AuthLoginRequestDTO("customer@example.com", "password")
        );

        assertThat(response.accessToken()).isEqualTo("token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        verify(authenticationManager).authenticate(any());
    }

    private UserPrincipalDTO principal() {
        return new UserPrincipalDTO(
                7L,
                "customer@example.com",
                null,
                true,
                List.of(Role.RoleCode.CUSTOMER),
                List.of(),
                List.of()
        );
    }
}
