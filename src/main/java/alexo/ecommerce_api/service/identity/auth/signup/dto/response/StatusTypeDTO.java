package alexo.ecommerce_api.service.identity.auth.signup.dto.response;

import alexo.ecommerce_api.entity.enums.UserStatusCode;

public record StatusTypeDTO(
        UserStatusCode code,
        String label,
        String description
) {
}
