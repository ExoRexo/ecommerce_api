package alexo.ecommerce_api.service.identity.user.dto.profile;

import alexo.ecommerce_api.entity.enums.UserStatusCode;

public record StatusTypeDTO(
        UserStatusCode code,
        String label,
        String description
) {
}
