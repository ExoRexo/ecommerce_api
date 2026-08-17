package alexo.ecommerce_api.repository.customer.order_item;

import java.math.BigDecimal;

public interface TotalAmountByOrderIdProjection {
    BigDecimal getPriceTotalRubSum();
    Long getOrderId();
}
