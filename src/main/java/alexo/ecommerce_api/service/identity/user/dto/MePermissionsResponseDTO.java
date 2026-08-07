package alexo.ecommerce_api.service.identity.user.dto;

import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;

import java.util.List;

public record MePermissionsResponseDTO(
        List<RoleCode> roles,
        List<PermissionCode> permissions
) {
}
