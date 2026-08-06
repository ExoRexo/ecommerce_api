package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.enums.contract.EnumCode;
import alexo.ecommerce_api.entity.enums.contract.EnumDescription;
import alexo.ecommerce_api.entity.enums.contract.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Reservation lifecycle statuses stored in order_item_reservation_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum OrderItemReservationStatusCode implements EnumCode, EnumLabel, EnumDescription {
    ACTIVE("Активно", "Резерв активен и удерживает складской остаток под позицию заказа."),
    CANCELLED("Отменено", "Резерв отменен, остаток возвращен в доступный."),
    FINISHED("Завершено", "Резерв использован при отгрузке и завершен.");

    private final String label;
    private final String description;
}
