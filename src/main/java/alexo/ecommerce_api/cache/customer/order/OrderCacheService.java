package alexo.ecommerce_api.cache.customer.order;

import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import alexo.ecommerce_api.entity.customer.order.OrderItemReservationStatusType;
import alexo.ecommerce_api.repository.customer.CustomerOrderStatusTypeRepository;
import alexo.ecommerce_api.repository.customer.OrderItemReservationStatusTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class OrderCacheService {

    private OrderItemReservationStatusTypeRepository orderItemReservationStatusTypeRepository;
    private CustomerOrderStatusTypeRepository customerOrderStatusTypeRepository;
    private static final String CUSTOMER_ORDER_ITEM_RESERVATION_STATUS_TYPE_CACHE_KEY = "customer.order.OrderItemReservationStatusType";
    private static final String CUSTOMER_ORDER_STATUS_TYPE_CACHE_KEY = "customer.order.CustomerOrderStatusCode";

    @Cacheable(CUSTOMER_ORDER_ITEM_RESERVATION_STATUS_TYPE_CACHE_KEY)
    public Map<OrderItemReservationStatusType.OrderItemReservationStatusCode, OrderItemReservationStatusType> getOrderItemReservationStatusTypes() {
        HashMap<OrderItemReservationStatusType.OrderItemReservationStatusCode, OrderItemReservationStatusType> statuses = new HashMap<>();

        orderItemReservationStatusTypeRepository.findAll().forEach(status -> statuses.put(status.getCode(), status));

        return statuses;
    }

    @Cacheable(CUSTOMER_ORDER_STATUS_TYPE_CACHE_KEY)
    public Map<CustomerOrderStatusType.CustomerOrderStatusCode, CustomerOrderStatusType> getCustomerOrderStatusTypes() {
        HashMap<CustomerOrderStatusType.CustomerOrderStatusCode, CustomerOrderStatusType> statuses = new HashMap<>();

        customerOrderStatusTypeRepository.findAll().forEach(status -> statuses.put(status.getCode(), status));

        return statuses;
    }
}
