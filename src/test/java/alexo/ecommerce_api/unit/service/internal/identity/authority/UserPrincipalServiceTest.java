import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.repository.identity.user.UserPrincipalRepository;
import alexo.ecommerce_api.service.internal.identity.authority.UserPrincipalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPrincipalServiceTest {

    @Mock
    private UserPrincipalRepository userPrincipalRepository;

    @InjectMocks
    private UserPrincipalService userPrincipalService;

    @Test
    void shouldBuildPrincipalWithDirectAndRolePermissions() {
        Permission directPermission = Permission.builder()
                .code(Permission.PermissionCode.CATALOG_PRODUCT_READ_LIST)
                .build();
        Permission rolePermission = Permission.builder()
                .code(Permission.PermissionCode.CATALOG_PRODUCT_CREATE)
                .build();
        Role role = Role.builder()
                .code(Role.RoleCode.MANAGER)
                .permissions(Set.of(rolePermission))
                .build();
        User user = User.builder()
                .id(7L)
                .email("manager@example.com")
                .passwordHash("hash")
                .roles(Set.of(role))
                .directPermissions(Set.of(directPermission))
                .build();

        when(userPrincipalRepository.findByEmailForUserDetails(user.getEmail()))
                .thenReturn(java.util.Optional.of(user));

        var principal = (alexo.ecommerce_api.dto.service.internal.identity.UserPrincipalDTO)
                userPrincipalService.loadUserByUsername(user.getEmail());

        assertThat(principal.getId()).isEqualTo(7L);
        assertThat(principal.getRoles()).containsExactly(Role.RoleCode.MANAGER);
        assertThat(principal.getPermissions())
                .containsExactlyInAnyOrder(
                        Permission.PermissionCode.CATALOG_PRODUCT_READ_LIST,
                        Permission.PermissionCode.CATALOG_PRODUCT_CREATE
                );
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        when(userPrincipalRepository.findByEmailForUserDetails("missing@example.com"))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> userPrincipalService.loadUserByUsername("missing@example.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
