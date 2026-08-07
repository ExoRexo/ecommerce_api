package alexo.ecommerce_api.cache.identity.permission;

import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.repository.identity.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PermissionCacheService {

    private final PermissionRepository permissionRepository;

    @Cacheable("user_permissions")
    public ConcurrentHashMap<PermissionCode, Permission> getPermissions() {
        ConcurrentHashMap<PermissionCode, Permission> permissions = new ConcurrentHashMap<>();

        permissionRepository.findAll().forEach(permission -> permissions.put(permission.getCode(), permission));

        return permissions;
    }
}
