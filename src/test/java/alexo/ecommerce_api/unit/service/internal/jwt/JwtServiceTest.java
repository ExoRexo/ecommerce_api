package alexo.ecommerce_api.service.internal.jwt;

import alexo.ecommerce_api.dto.configuration.security.jwt.JwtPropertiesDTO;
import alexo.ecommerce_api.dto.service.internal.identity.UserPrincipalDTO;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(new JwtPropertiesDTO(
                "test-secret-with-at-least-32-characters",
                3_600_000L
        ));
        jwtService.initializeKey();
    }

    @Test
    void shouldRoundTripPrincipalClaims() {
        UserPrincipalDTO principal = new UserPrincipalDTO(
                42L,
                "customer@example.com",
                null,
                true,
                List.of(Role.RoleCode.CUSTOMER),
                List.of(Permission.PermissionCode.CATALOG_PRODUCT_READ_LIST),
                List.of()
        );

        String token = jwtService.generateToken(principal);

        UserPrincipalDTO restored = jwtService.getPrincipalFromToken(token);

        assertThat(jwtService.isTokenValid(token)).isTrue();
        assertThat(restored.getId()).isEqualTo(42L);
        assertThat(restored.getUsername()).isEqualTo("customer@example.com");
        assertThat(restored.isEnabled()).isTrue();
        assertThat(restored.getRoles()).containsExactly(Role.RoleCode.CUSTOMER);
        assertThat(restored.getPermissions())
                .containsExactly(Permission.PermissionCode.CATALOG_PRODUCT_READ_LIST);
    }

    @Test
    void shouldRejectTamperedToken() {
        UserPrincipalDTO principal = new UserPrincipalDTO(
                42L,
                "customer@example.com",
                null,
                true,
                List.of(Role.RoleCode.CUSTOMER),
                List.of(),
                List.of()
        );

        String token = jwtService.generateToken(principal);
        String tamperedToken = token.substring(0, token.length() - 1)
                + (token.endsWith("a") ? "b" : "a");

        assertThat(jwtService.isTokenValid(tamperedToken)).isFalse();
    }

    @Test
    void shouldRejectShortSecret() {
        JwtService service = new JwtService(new JwtPropertiesDTO("short", 1_000L));

        assertThatThrownBy(service::initializeKey)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("at least 32 bytes");
    }
}
