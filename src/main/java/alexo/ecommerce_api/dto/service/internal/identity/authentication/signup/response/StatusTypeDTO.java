package alexo.ecommerce_api.dto.service.internal.identity.authentication.signup.response;

import alexo.ecommerce_api.entity.identity.UserStatusType;

public record StatusTypeDTO(
        UserStatusType.UserStatusCode code,
        String label,
        String description
) {
}
