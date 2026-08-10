package alexo.ecommerce_api.service.identity.authority;

import alexo.ecommerce_api.cache.identity.authority.PermissionCacheService;
import alexo.ecommerce_api.cache.identity.authority.RoleCacheService;
import alexo.ecommerce_api.entity.identity.*;
import alexo.ecommerce_api.enums.entity.PermissionCode;
import alexo.ecommerce_api.enums.entity.RoleCode;
import alexo.ecommerce_api.repository.identity.UserPermissionRepository;
import alexo.ecommerce_api.repository.identity.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthorityService {

    private final UserRoleRepository userRoleRepository;
    private final UserPermissionRepository userPermissionRepository;
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
    public List<UserRole> replaceUserRoles(@NotNull User user, @NotNull List<Role> roles) {
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
     * @param user user
     * @param roles roles to add
     * @return added roles
     */
    @Transactional
    public List<UserRole> addUserRoles(@NotNull User user, @NotNull List<Role> roles) {
        List<Role> existsRoles = userRoleRepository
                .findByRoleIdInAndUserId(
                        roles.stream().map(Role::getId).toList(),
                        user.getId()
                )
                .stream()
                .map(UserRole::getRole)
                .toList();

        List<UserRole> toSave = new ArrayList<>();

        for (Role role : roles) {

            if (existsRoles.contains(role)) {
                continue;
            }

            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(user);
            toSave.add(userRole);
        }

        return userRoleRepository.saveAll(toSave);
    }

    @Transactional
    public void removeUserRoles(@NotNull User user, @NotNull List<Role> roles) {
        userRoleRepository.deleteAllByUserIdAndRoleIdIn(user.getId(), roles.stream().map(Role::getId).toList());
    }

    /**
     * @param user user
     * @param permissions permissions to add
     * @return added permissions
     */
    @Transactional
    public List<UserPermission> addUserDirectPermissions(@NotNull User user, @NotNull List<Permission> permissions) {
        List<Permission> existsPermissions = userPermissionRepository
                .findByPermissionIdInAndUserId(
                        permissions.stream().map(Permission::getId).toList(),
                        user.getId()
                )
                .stream()
                .map(UserPermission::getPermission)
                .toList();

        List<UserPermission> toSave = new ArrayList<>();

        for (Permission permission : permissions) {

            if (existsPermissions.contains(permission)) {
                continue;
            }

            UserPermission userPermission = new UserPermission();
            userPermission.setPermission(permission);
            userPermission.setUser(user);
            toSave.add(userPermission);
        }

        return userPermissionRepository.saveAll(toSave);
    }

    /**
     * replace current user direct permissions to provided permissions
     *
     * @param user id of user
     * @param permissions direct permissions to set for user
     * @return resulted entities of user direct permissions
     */
    @Transactional
    public List<UserPermission> replaceUserDirectPermissions(@NotNull User user, @NotNull List<Permission> permissions) {
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

    @Transactional
    public void removeUserDirectPermissions(@NotNull User user, @NotNull List<Permission> permissions) {
        userPermissionRepository.deleteAllByUserIdAndPermissionIdIn(user.getId(), permissions.stream().map(Permission::getId).toList());
    }

}