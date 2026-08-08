package alexo.ecommerce_api.service.identity.user.dto.profile;

import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;

import java.util.List;
import java.util.Set;

public record ProfileResponseDTO(
        String email,
        String firstName,
        String lastName,
        StatusTypeDTO status,
        Set<PermissionCode> directPermissions,
        Set<PermissionCode> rolePermissions,
        List<RoleCode> roles
) {}
