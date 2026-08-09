package alexo.ecommerce_api.service.identity.authority;

import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.repository.identity.user.UserPrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {

    private final UserPrincipalRepository userPrincipalRepository;

    public @NotNull UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        return buildPrincipal(userPrincipalRepository.findByEmailForUserDetails(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found")));
    }

    public @NotNull UserDetails loadUserById(@NotNull Long userId) throws UsernameNotFoundException {
        return buildPrincipal(userPrincipalRepository.findByIdForUserDetails(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with userId " + userId + " not found")));
    }

    private UserPrincipal buildPrincipal(User user) {
        Set<Role> roles = user.getRoles();

        List<RoleCode> roleCodes = roles
                .stream()
                .map(Role::getCode)
                .toList();

        Set<PermissionCode> directPermissions = user
                .getDirectPermissions()
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        for (Role role : roles) {
            directPermissions.addAll(
                    role.getPermissions()
                            .stream()
                            .map(Permission::getCode)
                            .collect(Collectors.toSet())
            );
        }

        return UserPrincipal.from(user, roleCodes, directPermissions.stream().toList());
    }
}
