package alexo.ecommerce_api.dto.service.identity.authentication.signup;

import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.UserStatusType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public record UserCreationDTO(
        @NotNull String email,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull UserStatusType statusType,
        @NotNull HashSet<Role> roles,
        @NotNull HashSet<Permission> directPermissions,
        @NotNull String passwordHash
) {
}
