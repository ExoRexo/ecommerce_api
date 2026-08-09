package alexo.ecommerce_api.enums.entity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import alexo.ecommerce_api.contract.enums.EnumDescription;
import alexo.ecommerce_api.contract.enums.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * User account status values stored in user_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements EnumCode, EnumLabel, EnumDescription {
    ACTIVE("Активен", "Учетная запись пользователя активна и может входить в систему."),
    UNACTIVE("Неактивен", "Учетная запись пользователя неактивна, доступ ограничен.");

    private final String label;
    private final String description;
}
