package alexo.ecommerce_api.enums.entity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import alexo.ecommerce_api.contract.enums.EnumDescription;
import alexo.ecommerce_api.contract.enums.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Role values stored in roles.code.
 */
@Getter
@RequiredArgsConstructor
public enum RoleCode implements EnumCode, EnumLabel, EnumDescription {
    ADMIN("Администратор", "Администратор платформы с полным доступом."),
    MANAGER("Менеджер", "Менеджер операций с доступом к управлению каталогом и заказами."),
    CUSTOMER("Клиент", "Конечный пользователь для покупок и оформления заказов.");

    private final String label;
    private final String description;
}
