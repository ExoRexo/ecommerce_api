package alexo.ecommerce_api.unit.service.internal.inventory.product_stock_management;

import alexo.ecommerce_api.cache.inventory.warehouse.WarehouseCacheService;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseStockTransactionRepository;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import alexo.ecommerce_api.service.internal.inventory.product_stock_management.TransactionalProductStockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TransactionalProductStockServiceTest {
    @Mock private ProductRepository productRepository;
    @Mock private WarehouseRepository warehouseRepository;
    @Mock private WarehouseCacheService warehouseCacheService;
    @Mock private ProductWarehouseStockRepository stockRepository;
    @Mock private AuthorizationService authorizationService;
    @Mock private UserRepository userRepository;
    @Mock private WarehouseStockTransactionRepository transactionRepository;
    @InjectMocks private TransactionalProductStockService service;

    @Test
    void shouldRejectNullStockUpdate() {
        assertThatThrownBy(() -> service.updateProductPhysicalStockOnWarehouse(null))
                .isInstanceOf(NullPointerException.class);
    }
}