package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.enums.contract.EnumCode;
import alexo.ecommerce_api.entity.enums.contract.EnumDescription;
import alexo.ecommerce_api.entity.enums.contract.EnumLabel;
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
