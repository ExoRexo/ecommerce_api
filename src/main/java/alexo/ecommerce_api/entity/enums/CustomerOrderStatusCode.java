package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Business order statuses stored in customer_order_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum CustomerOrderStatusCode implements EnumCode {
    CREATED("CREATED"),
    PENDING_PAYMENT("PENDING_PAYMENT"),
    PAID("PAID"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private final String code;
}
