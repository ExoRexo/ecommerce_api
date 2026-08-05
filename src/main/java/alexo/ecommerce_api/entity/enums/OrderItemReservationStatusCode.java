package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Reservation lifecycle statuses stored in order_item_reservation_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum OrderItemReservationStatusCode implements EnumCode, EnumLabel, EnumDescription {
    ACTIVE("ACTIVE", "Активно", "Резерв активен и удерживает складской остаток под позицию заказа."),
    CANCELLED("CANCELLED", "Отменено", "Резерв отменен, остаток возвращен в доступный."),
    FINISHED("FINISHED", "Завершено", "Резерв использован при отгрузке и завершен.");

    private final String code;
    private final String label;
    private final String description;
}
