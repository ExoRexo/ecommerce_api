package alexo.ecommerce_api.dto.service.internal.identity.user.profile;

import alexo.ecommerce_api.entity.identity.UserStatusType;

public record StatusTypeDTO(
        UserStatusType.UserStatusCode code,
        String label,
        String description
) {
}
