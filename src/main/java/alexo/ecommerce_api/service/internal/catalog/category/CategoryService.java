package alexo.ecommerce_api.service.internal.catalog.category;

import alexo.ecommerce_api.cache.catalog.category.CategoryCacheService;
import alexo.ecommerce_api.dto.service.internal.catalog.category.tree.NodeDTO;
import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import alexo.ecommerce_api.dto.service.internal.catalog.category.CategoryResponseDTO;
import alexo.ecommerce_api.dto.service.internal.catalog.category.create.CreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.catalog.category.update.CategoryUpdateRequestDTO;
import jakarta.persistence.EntityExistsException;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    private CategoryCacheService categoryCacheService;

    /**
     * @param createRequestDTO category create request
     * @return category create response
     */
    @Transactional
    public CategoryResponseDTO createCategory(CreateRequestDTO createRequestDTO) {
        Objects.requireNonNull(createRequestDTO);

        Category category = new Category();

        Long parentId = createRequestDTO.parentId();

        if (parentId != null) {
            category.setParent(categoryRepository.findById(parentId).orElseThrow());
        }

        category.setName(createRequestDTO.name().trim());

        Optional<Category> existsCategory = categoryRepository.findByNameAndParentId(category.getName(), parentId);

        if (existsCategory.isPresent()) {
            String message;

            if (parentId == null) {
                message = "root category [" + existsCategory.get().getName() +  "] is already exists";
            } else {
                message = "category [" + category.getName() + "] with parent category [" + existsCategory.get().getParent().getName() +  "] is already exists";
            }

            throw new EntityExistsException(message);
        }

        categoryRepository.save(category);

        return new CategoryResponseDTO(
                category.getId(),
                categoryCacheService.getCategoryTree(category.getId()),
                parentId
        );
    }

    /**
     * @param updateRequestDTO update request
     * @return category response
     */
    @Transactional
    public CategoryResponseDTO updateCategory(CategoryUpdateRequestDTO updateRequestDTO) {
        Objects.requireNonNull(updateRequestDTO);

        Long categoryId = updateRequestDTO.categoryId();

        Category category = categoryRepository.findByIdForUpdate(categoryId).orElseThrow();

        if (updateRequestDTO.parentId().isPresent()) {
            Long parentId = updateRequestDTO.parentId().get();

            if (parentId == null) {
                category.setParent(null);
            } else {
                category.setParent(categoryRepository.getReferenceById(parentId));
            }
        }

        if (updateRequestDTO.name().isPresent()) {
            category.setName(updateRequestDTO.name().get().trim());
        }

        Category parent = category.getParent();

        Long parentId = null;
        if (parent != null) {
            parentId = parent.getId();
        }

        Optional<Category> alreadyExistsCategory = categoryRepository.findByNameAndIdIsNotAndParentIdIs(category.getName(), category.getId(), parentId);

        if (alreadyExistsCategory.isPresent()) {
            String message = "category with name [" + alreadyExistsCategory.get().getName() + "]";

            if (parent != null) {
                message += " and parent category name [" + parent.getName() + "]";
            }

            message += " is already exists";

            throw new EntityExistsException(message);
        }

        categoryRepository.save(category);

        categoryCacheService.evictAllCategoryTrees();

        return new CategoryResponseDTO(
                category.getId(),
                categoryCacheService.getCategoryTree(category.getId()),
                parentId
        );
    }

    /**
     * @return list of categories
     */
    public List<CategoryResponseDTO> getCategoryList() {
        Map<Long, String> categoryTrees = categoryCacheService.getCategoryTrees();
        return categoryRepository.findAll().stream().map(category -> new CategoryResponseDTO(
                category.getId(),
                Optional.ofNullable(categoryTrees.get(category.getId())).orElseThrow(),
                Optional.ofNullable(category.getParent())
                        .map(Category::getId)
                        .orElse(null)
        )).toList();
    }

    /**
     * @param categoryId pk of category
     * @return category
     */
    public CategoryResponseDTO getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        return new CategoryResponseDTO(
                category.getId(),
                categoryCacheService.getCategoryTree(category.getId()),
                Optional.ofNullable(category.getParent())
                        .map(Category::getId)
                        .orElse(null)
        );
    }

    /**
     * get categories tree
     * @return categories tree
     */
    public List<NodeDTO> getCategoryTree() {

        List<Category> categories = categoryRepository.findAllWithParent();

        Map<Long, List<Category>> childrenByParent = categories.stream()
                .filter(category -> category.getParent() != null)
                .collect(Collectors.groupingBy(
                        category -> category.getParent().getId()
                ));

        return categories.stream()
                .filter(category -> category.getParent() == null)
                .map(category -> toNode(category, childrenByParent))
                .toList();
    }

    private @NotNull NodeDTO toNode(
            @NotNull Category category,
            @NotNull Map<Long, List<Category>> childrenByParent
    ) {
        List<NodeDTO> children = childrenByParent
                .getOrDefault(category.getId(), List.of())
                .stream()
                .map(child -> toNode(child, childrenByParent))
                .toList();

        return new NodeDTO(
                category.getId(),
                category.getName(),
                children
        );
    }
}
