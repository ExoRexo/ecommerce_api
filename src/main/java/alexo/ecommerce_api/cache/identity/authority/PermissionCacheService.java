package alexo.ecommerce_api.cache.identity.authority;

import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.repository.identity.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class PermissionCacheService {

    private final PermissionRepository permissionRepository;

    @Cacheable("user_permissions")
    public ConcurrentHashMap<Permission.PermissionCode, Permission> getPermissions() {
        ConcurrentHashMap<Permission.PermissionCode, Permission> permissions = new ConcurrentHashMap<>();

        permissionRepository.findAll().forEach(permission -> permissions.put(permission.getCode(), permission));

        return permissions;
    }
}
