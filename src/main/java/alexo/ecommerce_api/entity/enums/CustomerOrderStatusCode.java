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
    CREATED("CREATED", "Created", "Order was created and is awaiting further processing."),
    PENDING_PAYMENT("PENDING_PAYMENT", "Pending Payment", "Order is waiting for a payment transaction."),
    PAID("PAID", "Paid", "Payment was received successfully for the order."),
    COMPLETED("COMPLETED", "Completed", "Order lifecycle is completed and closed."),
    CANCELLED("CANCELLED", "Cancelled", "Order was cancelled before completion.");

    private final String code;
    private final String label;
    private final String description;
}
