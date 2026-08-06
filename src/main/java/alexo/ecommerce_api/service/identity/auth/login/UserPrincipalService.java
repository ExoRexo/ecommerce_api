package alexo.ecommerce_api.service.identity.auth.login;

import alexo.ecommerce_api.entity.enums.contract.EnumCode;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.repository.identity.UserRepository;
import alexo.ecommerce_api.service.identity.permission.UserPermissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPermissionService userPermissionService;

    @Override
    public @NotNull UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));

        return buildPrincipal(user);
    }

    public UserPrincipal loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + userId + " not found"));

        return buildPrincipal(user);
    }

    private UserPrincipal buildPrincipal(User user) {
        List<String> roles = userPermissionService.getEffectiveRoles(user.getId())
                .stream()
                .map(Role::getCode)
                .map(EnumCode::getCode)
                .toList();

        List<String> permissions = userPermissionService.getEffectivePermissions(user.getId())
                .stream()
                .map(Permission::getCode)
                .map(EnumCode::getCode)
                .toList();

        return UserPrincipal.from(user, roles, permissions);
    }
}
