package alexo.ecommerce_api.enums.entity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Reservation lifecycle statuses stored in order_item_reservation_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum OrderItemReservationStatusCode implements EnumCode {
    ACTIVE,
    CANCELLED,
    FINISHED
}
