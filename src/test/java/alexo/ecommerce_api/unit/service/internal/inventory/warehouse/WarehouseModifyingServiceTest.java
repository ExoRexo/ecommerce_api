package alexo.ecommerce_api.unit.service.internal.inventory.warehouse;

import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.inventory.AddressRepository;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.repository.lock.AdvisoryLockRepository;
import alexo.ecommerce_api.service.internal.inventory.warehouse.WarehouseModifyingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class WarehouseModifyingServiceTest {
    @Mock private WarehouseRepository warehouseRepository;
    @Mock private AddressRepository addressRepository;
    @Mock private ProductRepository productRepository;
    @Mock private ProductWarehouseStockRepository stockRepository;
    @Mock private AdvisoryLockRepository advisoryLockRepository;
    @InjectMocks private WarehouseModifyingService service;

    @Test
    void shouldRejectNullCreateAndUpdateRequests() {
        assertThatThrownBy(() -> service.persistWarehouse(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> service.updateWarehouse(null)).isInstanceOf(NullPointerException.class);
    }
}