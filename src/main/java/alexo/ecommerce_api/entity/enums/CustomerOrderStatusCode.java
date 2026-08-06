package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.enums.contract.EnumCode;
import alexo.ecommerce_api.entity.enums.contract.EnumDescription;
import alexo.ecommerce_api.entity.enums.contract.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Business order statuses stored in customer_order_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum CustomerOrderStatusCode implements EnumCode, EnumLabel, EnumDescription {
    CREATED("Создан", "Заказ создан и ожидает дальнейшей обработки."),
    PENDING_PAYMENT("Ожидает оплаты", "Заказ ожидает проведения оплаты."),
    PAID("Оплачен", "Оплата по заказу успешно получена."),
    COMPLETED("Завершен", "Жизненный цикл заказа завершен."),
    CANCELLED("Отменен", "Заказ отменен до завершения.");

    private final String label;
    private final String description;
}
