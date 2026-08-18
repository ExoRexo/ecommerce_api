package alexo.ecommerce_api.unit.service.internal.inventory.product_stock_management;

import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseStockTransactionRepository;
import alexo.ecommerce_api.service.internal.inventory.product_stock_management.ProductStockManagementService;
import alexo.ecommerce_api.service.internal.inventory.product_stock_management.TransactionalProductStockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductStockManagementServiceTest {

    @Mock
    private TransactionalProductStockService transactionalProductStockService;

    @Mock
    private WarehouseStockTransactionRepository warehouseStockTransactionRepository;

    @Mock
    private ProductWarehouseStockRepository productWarehouseStockRepository;

    @InjectMocks
    private ProductStockManagementService productStockManagementService;

    @Test
    void shouldRejectNullStockUpdateRequest() {
        assertThatThrownBy(() ->
                productStockManagementService.updateProductPhysicalStockOnWarehouse(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullStockListRequest() {
        assertThatThrownBy(() ->
                productStockManagementService.getProductStockList(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullTransactionListRequest() {
        assertThatThrownBy(() ->
                productStockManagementService.getTransactionsList(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
