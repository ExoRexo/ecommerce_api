package alexo.ecommerce_api.dto.service.internal.identity.user;

import alexo.ecommerce_api.enums.entity.PermissionCode;
import alexo.ecommerce_api.enums.entity.RoleCode;

import java.util.List;

public record MeAuthoritiesResponseDTO(
        List<RoleCode> roles,
        List<PermissionCode> permissions
) {
}
