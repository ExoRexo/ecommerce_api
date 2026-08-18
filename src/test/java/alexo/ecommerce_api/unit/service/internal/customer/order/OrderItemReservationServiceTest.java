package alexo.ecommerce_api.unit.service.internal.customer.order;

import alexo.ecommerce_api.cache.customer.order.OrderCacheService;
import alexo.ecommerce_api.repository.customer.CustomerWalletTransactionRepository;
import alexo.ecommerce_api.repository.customer.OrderItemWarehouseReservationRepository;
import alexo.ecommerce_api.repository.customer.order_item.OrderItemRepository;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.service.internal.customer.order.OrderItemReservationService;
import alexo.ecommerce_api.service.internal.inventory.product_stock_management.ProductStockManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OrderItemReservationServiceTest {
    @Mock private OrderCacheService orderCacheService;
    @Mock private OrderItemWarehouseReservationRepository reservationRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private ProductWarehouseStockRepository stockRepository;
    @Mock private WarehouseRepository warehouseRepository;
    @Mock private ProductStockManagementService stockManagementService;
    @InjectMocks private OrderItemReservationService service;

    @Test
    void shouldRejectNullReservationRequest() {
        assertThatThrownBy(() -> service.createOrderItemReservation(null)).isInstanceOf(IllegalArgumentException.class);
    }
}