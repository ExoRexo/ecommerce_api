package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Role values stored in roles.code.
 */
@Getter
@RequiredArgsConstructor
public enum RoleCode implements EnumCode, EnumLabel, EnumDescription {
    ADMIN("ADMIN", "Администратор", "Администратор платформы с полным доступом."),
    MANAGER("MANAGER", "Менеджер", "Менеджер операций с доступом к управлению каталогом и заказами."),
    CUSTOMER("CUSTOMER", "Клиент", "Конечный пользователь для покупок и оформления заказов.");

    private final String code;
    private final String label;
    private final String description;
}
