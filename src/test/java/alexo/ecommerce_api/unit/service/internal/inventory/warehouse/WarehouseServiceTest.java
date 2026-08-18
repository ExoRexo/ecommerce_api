package alexo.ecommerce_api.unit.service.internal.inventory.warehouse;

import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.service.internal.inventory.warehouse.WarehouseModifyingService;
import alexo.ecommerce_api.service.internal.inventory.warehouse.WarehouseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {
    @Mock private WarehouseModifyingService warehouseModifyingService;
    @Mock private WarehouseRepository warehouseRepository;
    @InjectMocks private WarehouseService warehouseService;

    @Test
    void shouldRejectNullWarehouseListRequest() {
        assertThatThrownBy(() -> warehouseService.getWarehouseList(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullCreateAndUpdateRequests() {
        assertThatThrownBy(() -> warehouseService.createWarehouse(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> warehouseService.updateWarehouse(null)).isInstanceOf(NullPointerException.class);
    }
}