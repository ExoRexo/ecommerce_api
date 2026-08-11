package alexo.ecommerce_api.service.catalog.product;

import alexo.ecommerce_api.cache.catalog.category.CategoryCacheService;
import alexo.ecommerce_api.cache.catalog.product.ProductCacheService;
import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.product.ProductResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.request.ProductCreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.response.CategoryDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.response.StatusTypeDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.FiltersDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.PaginationDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.SortDTO;
import alexo.ecommerce_api.dto.service.catalog.product.update.request.ProductUpdateRequestDTO;
import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.entity.catalog.Product;
import alexo.ecommerce_api.entity.catalog.ProductStatusType;
import alexo.ecommerce_api.enums.entity.ProductStatusCode;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import alexo.ecommerce_api.specification.catalog.product.ProductSpecifications;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
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
        return getProductResponseDTO(product, null);
    }

    @NotNull
    private ProductResponseDTO getProductResponseDTO(Product product, Map<Long, String> categoryTrees) {
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
                        categoryTrees == null
                                ? categoryCacheService.getCategoryTree(product.getCategory().getId())
                                : categoryTrees.get(product.getCategory().getId()),
                        Optional.ofNullable(product.getCategory().getParent())
                                .map(Category::getId)
                                .orElse(null)
                )
        );
    }

    public PageResponseDTO<@NotNull ProductResponseDTO> getProductList(FiltersDTO filtersDTO, @Valid SortDTO sortDTO, @Valid PaginationDTO paginationDTO) {
        Assert.notNull(sortDTO, "sort must not be null");
        Assert.notNull(filtersDTO, "filters must not be null");
        Assert.notNull(paginationDTO, "pagination must not be null");

        List<Sort.Order> orders = new ArrayList<>();

        orders.add(new Sort.Order(sortDTO.direction(),sortDTO.field()));

        PageRequest pageRequest = PageRequest.of(
                paginationDTO.page(),
                paginationDTO.size(),
                Sort.by(orders)
        );

        Specification<@NotNull Product> specification = ProductSpecifications.ListSpecification.getSpecification(filtersDTO);

        Page<@NotNull Product> productPage = productRepository.findAll(specification, pageRequest);

        Map<Long, String> categoryTrees = categoryCacheService.getCategoryTrees(productPage.map(product -> product.getCategory().getId()).stream().toList());

        return PageResponseDTO.from(productPage.map(product -> getProductResponseDTO(product, categoryTrees)));
    }
}
