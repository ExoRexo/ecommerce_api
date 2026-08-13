package alexo.ecommerce_api.dto.service.internal.identity.user.roles;

import alexo.ecommerce_api.entity.identity.Role;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public record ReplaceUserRolesRequestDTO(
        @NotNull
        Long userId,

        @NotNull
        @UniqueElements
        List<Role.RoleCode> roleCodes
) {
}
