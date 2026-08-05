package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * User account status values stored in user_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements EnumCode {
    ACTIVE("ACTIVE"),
    UNACTIVE("UNACTIVE");

    private final String code;
}
