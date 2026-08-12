package alexo.ecommerce_api.dto.service.internal.identity.user.roles;

import alexo.ecommerce_api.enums.entity.RoleCode;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public record AddUserRolesRequestDTO(
        @NotNull
        Long userId,

        @NotNull
        @UniqueElements
        List<RoleCode> roleCodes
) {
}
