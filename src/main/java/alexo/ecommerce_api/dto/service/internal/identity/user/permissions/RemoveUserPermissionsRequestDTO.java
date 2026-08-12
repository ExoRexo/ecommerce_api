package alexo.ecommerce_api.dto.service.internal.identity.user.permissions;

import alexo.ecommerce_api.enums.entity.PermissionCode;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public record RemoveUserPermissionsRequestDTO(
        @NotNull
        Long userId,

        @NotNull
        @UniqueElements
        List<PermissionCode> permissionCodes
) {
}
