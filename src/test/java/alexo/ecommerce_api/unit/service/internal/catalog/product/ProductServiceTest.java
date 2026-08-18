package alexo.ecommerce_api.unit.service.internal.catalog.product;

import alexo.ecommerce_api.cache.catalog.category.CategoryCacheService;
import alexo.ecommerce_api.cache.catalog.product.ProductCacheService;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.repository.lock.AdvisoryLockRepository;
import alexo.ecommerce_api.service.internal.catalog.product.CodeGenerator;
import alexo.ecommerce_api.service.internal.catalog.product.ProductService;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock private CodeGenerator codeGenerator;
    @Mock private ProductCacheService productCacheService;
    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private CategoryCacheService categoryCacheService;
    @Mock private AuthorizationService authorizationService;
    @Mock private WarehouseRepository warehouseRepository;
    @Mock private ProductWarehouseStockRepository stockRepository;
    @Mock private AdvisoryLockRepository advisoryLockRepository;
    @InjectMocks private ProductService service;

    @Test
    void shouldRejectNullListArguments() {
        assertThatThrownBy(() -> service.getProductList(null, null, null)).isInstanceOf(IllegalArgumentException.class);
    }
}