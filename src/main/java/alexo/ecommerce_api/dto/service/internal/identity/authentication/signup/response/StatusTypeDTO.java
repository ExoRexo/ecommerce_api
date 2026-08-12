package alexo.ecommerce_api.dto.service.internal.identity.authentication.signup.response;

import alexo.ecommerce_api.enums.entity.UserStatusCode;

public record StatusTypeDTO(
        UserStatusCode code,
        String label,
        String description
) {
}
