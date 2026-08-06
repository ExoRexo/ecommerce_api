package alexo.ecommerce_api.service.identity.user.creation.dto;

import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public record UserCreationRequestDTO(
        @NotNull String email,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String password,
        @NotNull ArrayList<RoleCode> roleCodesAdditional,
        @NotNull ArrayList<PermissionCode> permissionCodesAdditional
) {
}
