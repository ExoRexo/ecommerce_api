package alexo.ecommerce_api.dto.service.internal.identity.user;

import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;

import java.util.List;

public record MeAuthoritiesResponseDTO(
        List<Role.RoleCode> roles,
        List<Permission.PermissionCode> permissions
) {
}
