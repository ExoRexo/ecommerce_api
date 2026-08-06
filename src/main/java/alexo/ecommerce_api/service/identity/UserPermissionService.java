package alexo.ecommerce_api.service.identity;

import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.repository.identity.RolePermissionRepository;
import alexo.ecommerce_api.repository.identity.UserPermissionRepository;
import alexo.ecommerce_api.repository.identity.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
final public class UserPermissionService {

    private final UserRoleRepository userRoleRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * @param userId user identifier
     * @return all unique permissions from roles and direct permissions
     */
    public @NotNull Set<Permission> getEffectivePermissions(@NotNull Long userId) {

        Set<Permission> result = new LinkedHashSet<>(userPermissionRepository.findDirectPermissionsByUserId(userId));

        List<Role> roles = userRoleRepository.findRolesByUserId(userId);
        if (!roles.isEmpty()) {
            Set<Integer> roleIds = roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());

            result.addAll(rolePermissionRepository.findPermissionsByRoleIds(roleIds));
        }

        return result;
    }
}