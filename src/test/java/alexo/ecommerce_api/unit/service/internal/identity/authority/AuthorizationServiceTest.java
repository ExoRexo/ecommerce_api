package alexo.ecommerce_api.unit.service.internal.identity.authority;

import alexo.ecommerce_api.dto.service.internal.identity.UserPrincipalDTO;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthorizationServiceTest {

    private final AuthorizationService authorizationService = new AuthorizationService();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReadCurrentUserAndAuthorities() {
        UserPrincipalDTO principal = new UserPrincipalDTO(
                7L,
                "manager@example.com",
                null,
                true,
                List.of(Role.RoleCode.MANAGER),
                List.of(Permission.PermissionCode.CATALOG_PRODUCT_READ_LIST),
                List.of(
                        new SimpleGrantedAuthority("ROLE_MANAGER"),
                        new SimpleGrantedAuthority("PERMISSION_CATALOG_PRODUCT_READ_LIST")
                )
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );

        assertThat(authorizationService.getCurrentUserIdFromAuthentication()).isEqualTo(7L);
        assertThat(authorizationService.hasRoleAuthority(Role.RoleCode.MANAGER)).isTrue();
        assertThat(authorizationService.hasPermissionAuthority(
                Permission.PermissionCode.CATALOG_PRODUCT_READ_LIST
        )).isTrue();
        assertThat(authorizationService.hasRoleAuthority(Role.RoleCode.ADMIN)).isFalse();
    }

    @Test
    void shouldRejectMissingAuthentication() {
        assertThatThrownBy(authorizationService::getCurrentUserPrincipalFromAuthentication)
                .isInstanceOf(InsufficientAuthenticationException.class);
    }
}
