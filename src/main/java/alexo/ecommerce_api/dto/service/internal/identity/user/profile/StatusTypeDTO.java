package alexo.ecommerce_api.dto.service.internal.identity.user.profile;

import alexo.ecommerce_api.enums.entity.UserStatusCode;

public record StatusTypeDTO(
        UserStatusCode code,
        String label,
        String description
) {
}
