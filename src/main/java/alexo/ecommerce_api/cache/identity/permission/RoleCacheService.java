package alexo.ecommerce_api.cache.identity.permission;

import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.repository.identity.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RoleCacheService {

    private final RoleRepository roleRepository;

    @Cacheable("user_roles")
    public ConcurrentHashMap<RoleCode, Role> getRoles() {
        ConcurrentHashMap<RoleCode, Role> roles = new ConcurrentHashMap<>();

        roleRepository.findAll().forEach(role -> roles.put(role.getCode(), role));

        return roles;
    }
}
