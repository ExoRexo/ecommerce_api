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
    ACTIVE("ACTIVE", "Active", "Reservation is active and blocks stock for the order item."),
    CANCELLED("CANCELLED", "Cancelled", "Reservation was cancelled and stock was released."),
    FINISHED("FINISHED", "Finished", "Reservation was consumed during fulfillment and is finished.");

    private final String code;
    private final String label;
    private final String description;
}
