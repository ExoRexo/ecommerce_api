package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * User account status values stored in user_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements EnumCode, EnumLabel, EnumDescription {
    ACTIVE("ACTIVE", "Active", "User account is active and allowed to sign in."),
    UNACTIVE("UNACTIVE", "Inactive", "User account is inactive and access is restricted.");

    private final String code;
    private final String label;
    private final String description;
}
