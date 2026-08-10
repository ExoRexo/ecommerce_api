package alexo.ecommerce_api.cache.catalog.product;

import alexo.ecommerce_api.entity.catalog.ProductStatusType;
import alexo.ecommerce_api.enums.entity.ProductStatusCode;
import alexo.ecommerce_api.repository.catalog.ProductStatusTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductCacheService {

    private ProductStatusTypeRepository productStatusTypeRepository;
    private static final String PRODUCT_STATUS_CACHE_KEY = "catalog.category.productStatus";

    @Cacheable(PRODUCT_STATUS_CACHE_KEY)
    public Map<ProductStatusCode, ProductStatusType> getProductStatuses() {
        HashMap<ProductStatusCode, ProductStatusType> statuses = new HashMap<>();

        productStatusTypeRepository.findAll().forEach(status -> statuses.put(status.getCode(), status));

        return statuses;
    }
}
