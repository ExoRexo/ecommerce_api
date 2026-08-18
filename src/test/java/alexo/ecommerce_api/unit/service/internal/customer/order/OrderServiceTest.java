package alexo.ecommerce_api.unit.service.internal.customer.order;

import alexo.ecommerce_api.cache.customer.order.OrderCacheService;
import alexo.ecommerce_api.repository.customer.CartItemRepository;
import alexo.ecommerce_api.repository.customer.CustomerOrderRepository;
import alexo.ecommerce_api.repository.customer.CustomerRepository;
import alexo.ecommerce_api.repository.customer.order_item.OrderItemRepository;
import alexo.ecommerce_api.service.internal.customer.order.OrderItemReservationService;
import alexo.ecommerce_api.service.internal.customer.order.OrderService;
import alexo.ecommerce_api.service.internal.customer.order.OrderTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock private OrderItemReservationService reservationService;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private OrderCacheService orderCacheService;
    @Mock private CustomerOrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private OrderTransactionService transactionService;
    @InjectMocks private OrderService service;

    @Test
    void shouldRejectNullOrderCreationCustomer() {
        assertThatThrownBy(() -> service.createOrder(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullOrderListRequest() {
        assertThatThrownBy(() -> service.getOrderList(null)).isInstanceOf(IllegalArgumentException.class);
    }
}