package alexo.ecommerce_api.service.identity.auth.login;

import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.repository.identity.user.UserPrincipalRepository;
import alexo.ecommerce_api.service.identity.permission.UserPermissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {

    private final UserPrincipalRepository userPrincipalRepository;
    private final UserPermissionService userPermissionService;

    @Override
    @Transactional
    public @NotNull UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        User user = userPrincipalRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));

        return buildPrincipal(user);
    }

    private UserPrincipal buildPrincipal(User user) {
        List<Role> roles = userPermissionService.getEffectiveRoles(user.getId());

        List<RoleCode> roleCodes = roles
                .stream()
                .map(Role::getCode)
                .toList();

        List<PermissionCode> permissions = userPermissionService.getEffectivePermissions(user.getId(), roles)
                .stream()
                .map(Permission::getCode)
                .toList();

        return UserPrincipal.from(user, roleCodes, permissions);
    }
}
