package alexo.ecommerce_api.unit.service.internal.identity.authority;

import alexo.ecommerce_api.cache.identity.authority.PermissionCacheService;
import alexo.ecommerce_api.cache.identity.authority.RoleCacheService;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.repository.identity.UserPermissionRepository;
import alexo.ecommerce_api.repository.identity.UserRoleRepository;
import alexo.ecommerce_api.service.internal.identity.authority.UserAuthorityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthorityServiceTest {
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private UserPermissionRepository userPermissionRepository;
    @Mock private RoleCacheService roleCacheService;
    @Mock private PermissionCacheService permissionCacheService;
    @InjectMocks private UserAuthorityService service;

    @Test
    void shouldReplaceUserRolesAndPersistOneLinkPerRole() {
        User user = User.builder().id(7L).build();
        Role role = Role.builder().id(3).build();
        when(userRoleRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.replaceUserRoles(user, List.of(role));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUser()).isSameAs(user);
        assertThat(result.getFirst().getRole()).isSameAs(role);
        verify(userRoleRepository).deleteAllByUserId(7L);
        verify(userRoleRepository).saveAll(anyList());
    }

    @Test
    void shouldReadRolesFromCache() {
        HashMap<Role.RoleCode, Role> roles = new HashMap<>();
        roles.put(Role.RoleCode.CUSTOMER, Role.builder().id(1).build());

        doReturn(roles).when(roleCacheService).getRoles();
        assertThat(service.getRoleByCode(Role.RoleCode.CUSTOMER)).isNotNull();
    }
}