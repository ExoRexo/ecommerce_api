package alexo.ecommerce_api.service.catalog.product;

import alexo.ecommerce_api.cache.catalog.category.CategoryCacheService;
import alexo.ecommerce_api.cache.catalog.product.ProductCacheService;
import alexo.ecommerce_api.dto.service.catalog.product.create.request.ProductCreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.response.CategoryDTO;
import alexo.ecommerce_api.dto.service.catalog.product.ProductResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.response.StatusTypeDTO;
import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.entity.catalog.Product;
import alexo.ecommerce_api.enums.entity.ProductStatusCode;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

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
