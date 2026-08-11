package alexo.ecommerce_api.service.catalog.product;

import alexo.ecommerce_api.cache.catalog.category.CategoryCacheService;
import alexo.ecommerce_api.cache.catalog.product.ProductCacheService;
import alexo.ecommerce_api.dto.service.catalog.product.create.request.ProductCreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.response.CategoryDTO;
import alexo.ecommerce_api.dto.service.catalog.product.ProductResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.response.StatusTypeDTO;
import alexo.ecommerce_api.dto.service.catalog.product.update.request.ProductUpdateRequestDTO;
import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.entity.catalog.Product;
import alexo.ecommerce_api.entity.catalog.ProductStatusType;
import alexo.ecommerce_api.enums.entity.ProductStatusCode;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final CodeGenerator codeGenerator;
    private final ProductCacheService productCacheService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryCacheService categoryCacheService;

    @Transactional
    public ProductResponseDTO createProduct(@NotNull ProductCreateRequestDTO request) {

        if (productRepository.existsByNameAndCategory_Id(request.name(), request.categoryId())) {
            throw new EntityExistsException("product with name [" + request.name() + "] and category id [" + request.categoryId() +"] is already exists");
        }

        Product product = productRepository.save(
            Product.builder()
                    .name(request.name())
                    .description(request.description())
                    .priceRub(request.priceRub())
                    .code(codeGenerator.generateCode())
                    .statusType(Optional.ofNullable(productCacheService.getProductStatuses().get(ProductStatusCode.ACTIVE)).orElseThrow())
                    .category(categoryRepository.findById(request.categoryId()).orElseThrow())
                    .build()
        );

        return getProductResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO updateProduct(ProductUpdateRequestDTO request) {
        Product product = productRepository.findByIdForUpdate(request.productId()).orElseThrow((() -> new EntityNotFoundException("product with id [" + request.productId() + "] is not found")));

        Category category = product.getCategory();
        String name = product.getName();
        String description = product.getDescription();
        BigDecimal priceRub = product.getPriceRub();
        ProductStatusType statusType = product.getStatusType();

        if (request.categoryId().isPresent()) {
            category = categoryRepository.findById(request.categoryId().get()).orElseThrow();
        }

        if (request.name().isPresent()) {
            name = request.name().get();
        }

        if (request.description().isPresent()) {
            description = request.description().get();
        }

        if (request.priceRub().isPresent()) {
            priceRub = request.priceRub().get();
        }

        if (request.statusCode().isPresent()) {
            statusType = Optional
                    .ofNullable(
                        productCacheService
                                .getProductStatuses()
                                .get(
                                        request.statusCode().get()
                                )
                    )
                    .orElseThrow();
        }

        if (productRepository.existsByIdNotAndNameAndCategory_Id(product.getId(), name, category.getId())) {
            throw new EntityExistsException("product with name [" + product.getName() + "] and category id [" + product.getCategory().getId() +"] is already exists");
        }

        product.setCategory(category);
        product.setName(name);
        product.setDescription(description);
        product.setPriceRub(priceRub);
        product.setStatusType(statusType);

        return getProductResponseDTO(product);
    }

    @NotNull
    private ProductResponseDTO getProductResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getDescription(),
                product.getPriceRub(),
                product.getCreatedAt(),
                new StatusTypeDTO(
                        product.getStatusType().getCode(),
                        product.getStatusType().getLabel(),
                        product.getStatusType().getDescription()
                ),
                new CategoryDTO(
                        product.getCategory().getId(),
                        categoryCacheService.getCategoryTree(product.getCategory().getId()),
                        Optional.ofNullable(product.getCategory().getParent())
                                .map(Category::getId)
                                .orElse(null)
                )
        );
    }

}
