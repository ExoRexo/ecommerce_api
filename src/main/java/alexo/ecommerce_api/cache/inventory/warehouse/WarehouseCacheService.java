package alexo.ecommerce_api.cache.inventory.warehouse;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import alexo.ecommerce_api.repository.inventory.WarehouseStockTransactionPurposeTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class WarehouseCacheService {

    private WarehouseStockTransactionPurposeTypeRepository warehouseStockTransactionPurposeTypeRepository;
    private static final String WAREHOUSE_STOCK_TRANSACTION_PURPOSE_TYPE_CACHE_KEY = "inventory.warehouse.warehouseStockTransactionPurposeType";

    @Cacheable(WAREHOUSE_STOCK_TRANSACTION_PURPOSE_TYPE_CACHE_KEY)
    public Map<WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode, WarehouseStockTransactionPurposeType> getWarehouseStockTransactionPurposeTypes() {
        HashMap<WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode, WarehouseStockTransactionPurposeType> purposes = new HashMap<>();

        warehouseStockTransactionPurposeTypeRepository.findAll().forEach(status -> purposes.put(status.getCode(), status));

        return purposes;
    }
}
