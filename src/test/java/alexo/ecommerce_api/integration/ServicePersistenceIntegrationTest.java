package alexo.ecommerce_api.integration;

import alexo.ecommerce_api.dto.service.internal.catalog.category.create.CreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.WarehouseCreateRequestDTO;
import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.service.internal.catalog.category.CategoryService;
import alexo.ecommerce_api.service.internal.inventory.warehouse.WarehouseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

class ServicePersistenceIntegrationTest extends PostgresIntegrationTest {

    @Autowired private CatalogInventoryFixture catalogInventoryFixture;
    @Autowired private CustomerOrderFixture customerOrderFixture;
    @Autowired private CategoryService categoryService;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private WarehouseService warehouseService;
    @Autowired private WarehouseRepository warehouseRepository;

    @Test
    @Transactional
    void shouldPersistCategoryAndBuildItsTreeThroughFlywaySchema() {
        String name = "Integration category " + System.nanoTime();

        var response = categoryService.createCategory(new CreateRequestDTO(name, null));

        assertThat(response.id()).isNotNull();
        assertThat(response.treeName()).isEqualTo(name);
        assertThat(categoryRepository.findById(response.id())).get().extracting(Category::getName).isEqualTo(name);
    }

    @Test
    @Transactional
    void shouldPersistWarehouseAndAddressThroughFlywaySchema() {
        String name = "Warehouse " + System.nanoTime();
        var address = new WarehouseCreateRequestDTO.AddressDTO(
                "Integration street " + System.nanoTime(), "101000", "Russia", "Moscow");

        var response = warehouseService.createWarehouse(new WarehouseCreateRequestDTO(address, name));

        assertThat(response.id()).isNotNull();
        assertThat(warehouseRepository.findById(response.id())).get().extracting(Warehouse::getName).isEqualTo(name);
    }

    @Test
    @Transactional
    void shouldCreateReusableCatalogInventoryFixture() {
        CatalogInventoryFixture.CatalogInventoryData data =
                catalogInventoryFixture.createCatalogInventory();

        assertThat(data.category().getId()).isNotNull();
        assertThat(data.product().getId()).isNotNull();
        assertThat(data.warehouse().getId()).isNotNull();
        assertThat(data.stock().getId()).isNotNull();
        assertThat(data.stock().getPhysicalQuantity()).isEqualTo(10);
    }

    @Test
    @Transactional
    void shouldCreateReusableCustomerOrderFixture() {
        CatalogInventoryFixture.CatalogInventoryData catalogData =
                catalogInventoryFixture.createCatalogInventory();

        CustomerOrderFixture.CustomerOrderData data = customerOrderFixture
                .createCustomerWithProduct(catalogData, 2);

        assertThat(data.user().getId()).isNotNull();
        assertThat(data.customer().getUserId()).isEqualTo(data.user().getId());
        assertThat(data.cart().getCustomerId()).isEqualTo(data.customer().getUserId());
        assertThat(data.wallet().getCustomerId()).isEqualTo(data.customer().getUserId());
        assertThat(data.cartItem().getProduct().getId()).isEqualTo(catalogData.product().getId());
        assertThat(data.cartItem().getQuantity()).isEqualTo(2);
    }
}