package alexo.ecommerce_api.cache.identity.authority;

import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.repository.identity.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class RoleCacheService {

    private final RoleRepository roleRepository;

    @Cacheable("user_roles")
    public HashMap<Role.RoleCode, Role> getRoles() {
        HashMap<Role.RoleCode, Role> roles = new HashMap<>();

        roleRepository.findAll().forEach(role -> roles.put(role.getCode(), role));

        return roles;
    }
}
