package alexo.ecommerce_api.integration;

import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.entity.catalog.Product;
import alexo.ecommerce_api.entity.catalog.ProductStatusType;
import alexo.ecommerce_api.entity.inventory.Address;
import alexo.ecommerce_api.entity.inventory.ProductWarehouseStock;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.catalog.ProductStatusTypeRepository;
import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import alexo.ecommerce_api.repository.inventory.AddressRepository;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CatalogInventoryFixture {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductStatusTypeRepository productStatusTypeRepository;
    private final AddressRepository addressRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductWarehouseStockRepository stockRepository;

    @Transactional
    public CatalogInventoryData createCatalogInventory() {
        Category category = categoryRepository.save(Category.builder()
                .name("Fixture category " + System.nanoTime())
                .build());

        ProductStatusType activeStatus = productStatusTypeRepository.findAll().stream()
                .filter(status -> status.getCode() == ProductStatusType.ProductStatusCode.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("ACTIVE product status is not seeded"));

        Product product = productRepository.save(Product.builder()
                .name("Fixture product " + System.nanoTime())
                .code("FIX-" + System.nanoTime())
                .description("Product created by integration fixture")
                .priceRub(new BigDecimal("100.00"))
                .statusType(activeStatus)
                .category(category)
                .build());

        Address address = addressRepository.save(Address.builder()
                .address("Fixture address " + System.nanoTime())
                .mailIndex("101000")
                .country("Russia")
                .city("Moscow")
                .build());

        Warehouse warehouse = warehouseRepository.save(Warehouse.builder()
                .name("Fixture warehouse " + System.nanoTime())
                .address(address)
                .build());

        ProductWarehouseStock stock = stockRepository.save(ProductWarehouseStock.builder()
                .product(product)
                .warehouse(warehouse)
                .physicalQuantity(10)
                .reservedQuantity(0)
                .build());

        return new CatalogInventoryData(category, product, warehouse, stock);
    }

    public record CatalogInventoryData(
            Category category,
            Product product,
            Warehouse warehouse,
            ProductWarehouseStock stock
    ) {
    }
}
