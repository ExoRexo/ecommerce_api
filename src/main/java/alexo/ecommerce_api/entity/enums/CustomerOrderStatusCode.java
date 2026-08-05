package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Business order statuses stored in customer_order_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum CustomerOrderStatusCode implements EnumCode, EnumLabel, EnumDescription {
    CREATED("CREATED", "Создан", "Заказ создан и ожидает дальнейшей обработки."),
    PENDING_PAYMENT("PENDING_PAYMENT", "Ожидает оплаты", "Заказ ожидает проведения оплаты."),
    PAID("PAID", "Оплачен", "Оплата по заказу успешно получена."),
    COMPLETED("COMPLETED", "Завершен", "Жизненный цикл заказа завершен."),
    CANCELLED("CANCELLED", "Отменен", "Заказ отменен до завершения.");

    private final String code;
    private final String label;
    private final String description;
}
