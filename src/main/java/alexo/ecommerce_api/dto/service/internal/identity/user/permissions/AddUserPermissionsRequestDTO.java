package alexo.ecommerce_api.dto.service.internal.identity.user.permissions;

import alexo.ecommerce_api.entity.identity.Permission;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public record AddUserPermissionsRequestDTO(
        @NotNull
        Long userId,

        @NotNull
        @UniqueElements
        List<Permission.PermissionCode> permissionCodes
) {
}
