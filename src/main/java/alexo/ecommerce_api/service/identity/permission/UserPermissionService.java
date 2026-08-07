package alexo.ecommerce_api.service.identity.permission;

import alexo.ecommerce_api.cache.identity.permission.PermissionCacheService;
import alexo.ecommerce_api.cache.identity.permission.RoleCacheService;
import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.entity.identity.*;
import alexo.ecommerce_api.repository.identity.RolePermissionRepository;
import alexo.ecommerce_api.repository.identity.UserPermissionRepository;
import alexo.ecommerce_api.repository.identity.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPermissionService {

    private final UserRoleRepository userRoleRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleCacheService roleCacheService;
    private final PermissionCacheService permissionCacheService;

    /**
     * @param code permissions of the user
     * @return nullable, cached entity of permission
     */
    public Permission getPermissionByCode(PermissionCode code) {
        return this.permissionCacheService.getPermissions().get(code);
    }

    /**
     * @param code role of the user
     * @return nullable, cached entity of role
     */
    public Role getRoleByCode(RoleCode code) {
        return this.roleCacheService.getRoles().get(code);
    }

    /**
     * replace current user roles to provided roles
     *
     * @param user id of user
     * @param roles direct roles to set for user
     * @return resulted entities of user direct roles
     */
    @Transactional
    public List<UserRole> replaceUserRoles(@NotNull User user, @NotNull ArrayList<Role> roles) {
        userRoleRepository.deleteAllByUserId(user.getId());

        List<UserRole> userRoles = new ArrayList<>(roles.size());

        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(user);

            userRoles.add(userRole);
        }

        return userRoleRepository.saveAll(userRoles);
    }

    /**
     * replace current user direct permissions to provided permissions
     *
     * @param user id of user
     * @param permissions direct permissions to set for user
     * @return resulted entities of user direct permissions
     */
    @Transactional
    public List<UserPermission> replaceUserDirectPermissions(@NotNull User user, @NotNull ArrayList<Permission> permissions) {
        userPermissionRepository.deleteAllByUserId(user.getId());

        List<UserPermission> userPermissions = new ArrayList<>(permissions.size());

        for (Permission permission : permissions) {
            UserPermission userPermission = new UserPermission();
            userPermission.setPermission(permission);
            userPermission.setUser(user);

            userPermissions.add(userPermission);
        }

        return userPermissionRepository.saveAll(userPermissions);
    }

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

    /**
     * @param userId user identifier
     * @return all unique roles
     */
    public @NotNull List<Role> getEffectiveRoles(@NotNull Long userId) {
        return userRoleRepository.findRolesByUserId(userId);
    }
}