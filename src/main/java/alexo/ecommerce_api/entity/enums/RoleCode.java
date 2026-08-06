package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.enums.contract.EnumCode;
import alexo.ecommerce_api.entity.enums.contract.EnumDescription;
import alexo.ecommerce_api.entity.enums.contract.EnumLabel;
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
