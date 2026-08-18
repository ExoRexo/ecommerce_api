package alexo.ecommerce_api.unit.service.internal.catalog.category;

import alexo.ecommerce_api.cache.catalog.category.CategoryCacheService;
import alexo.ecommerce_api.dto.service.internal.catalog.category.CategoryResponseDTO;
import alexo.ecommerce_api.dto.service.internal.catalog.category.create.CreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.catalog.category.tree.NodeDTO;
import alexo.ecommerce_api.dto.service.internal.catalog.category.update.CategoryUpdateRequestDTO;
import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import alexo.ecommerce_api.service.internal.catalog.category.CategoryService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Tests")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryCacheService categoryCacheService;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;
    private Category parentCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Electronics")
                .parent(null)
                .build();

        parentCategory = Category.builder()
                .id(2L)
                .name("Devices")
                .parent(null)
                .build();
    }

    @Nested
    @DisplayName("createCategory Tests")
    class CreateCategoryTests {

        @Test
        @DisplayName("Should create root category successfully")
        void shouldCreateRootCategorySuccessfully() {
            // Arrange
            CreateRequestDTO createRequest = new CreateRequestDTO("Electronics", null);
            when(categoryRepository.findByNameAndParentId("Electronics", null))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class)))
                    .thenAnswer(invocation -> {
                        Category category = invocation.getArgument(0);
                        category.setId(1L);
                        return category;
                    });
            when(categoryCacheService.getCategoryTree(1L))
                    .thenReturn("Electronics");

            // Act
            CategoryResponseDTO result = categoryService.createCategory(createRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.treeName()).isEqualTo("Electronics");
            assertThat(result.parentId()).isNull();
            verify(categoryRepository).save(any(Category.class));
            verify(categoryCacheService).getCategoryTree(1L);
        }

        @Test
        @DisplayName("Should create subcategory with parent successfully")
        void shouldCreateSubcategoryWithParentSuccessfully() {
            // Arrange
            CreateRequestDTO createRequest = new CreateRequestDTO("Phones", 2L);

            when(categoryRepository.findById(2L))
                    .thenReturn(Optional.of(parentCategory));
            when(categoryRepository.findByNameAndParentId("Phones", 2L))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class)))
                    .thenAnswer(invocation -> {
                        Category category = invocation.getArgument(0);
                        category.setId(3L);
                        return category;
                    });
            when(categoryCacheService.getCategoryTree(3L))
                    .thenReturn("Devices > Phones");

            // Act
            CategoryResponseDTO result = categoryService.createCategory(createRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(3L);
            assertThat(result.treeName()).isEqualTo("Devices > Phones");
            assertThat(result.parentId()).isEqualTo(2L);
            verify(categoryRepository).findById(2L);
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        @DisplayName("Should trim whitespace from category name")
        void shouldTrimWhitespaceFromCategoryName() {
            // Arrange
            CreateRequestDTO createRequest = new CreateRequestDTO("  Electronics  ", null);
            when(categoryRepository.findByNameAndParentId("Electronics", null))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class)))
                    .thenAnswer(invocation -> {
                        Category category = invocation.getArgument(0);
                        category.setId(1L);
                        return category;
                    });
            when(categoryCacheService.getCategoryTree(1L))
                    .thenReturn("Electronics");

            // Act
            categoryService.createCategory(createRequest);

            // Assert
            verify(categoryRepository).save(argThat(category ->
                    "Electronics".equals(category.getName())
            ));
        }

        @Test
        @DisplayName("Should throw EntityExistsException when root category already exists")
        void shouldThrowExceptionWhenRootCategoryAlreadyExists() {
            // Arrange
            CreateRequestDTO createRequest = new CreateRequestDTO("Electronics", null);
            when(categoryRepository.findByNameAndParentId("Electronics", null))
                    .thenReturn(Optional.of(testCategory));

            // Act & Assert
            assertThatThrownBy(() -> categoryService.createCategory(createRequest))
                    .isInstanceOf(EntityExistsException.class)
                    .hasMessageContaining("root category")
                    .hasMessageContaining("Electronics")
                    .hasMessageContaining("already exists");
            verify(categoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw EntityExistsException when subcategory already exists")
        void shouldThrowExceptionWhenSubcategoryAlreadyExists() {
            // Arrange
            CreateRequestDTO createRequest = new CreateRequestDTO("Phones", 2L);
            Category existingPhone = Category.builder()
                    .id(4L)
                    .name("Phones")
                    .parent(parentCategory)
                    .build();

            when(categoryRepository.findById(2L))
                    .thenReturn(Optional.of(parentCategory));
            when(categoryRepository.findByNameAndParentId("Phones", 2L))
                    .thenReturn(Optional.of(existingPhone));

            // Act & Assert
            assertThatThrownBy(() -> categoryService.createCategory(createRequest))
                    .isInstanceOf(EntityExistsException.class)
                    .hasMessageContaining("Phones")
                    .hasMessageContaining("Devices")
                    .hasMessageContaining("already exists");
            verify(categoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw NoSuchElementException when parent category not found")
        void shouldThrowExceptionWhenParentCategoryNotFound() {
            // Arrange
            CreateRequestDTO createRequest = new CreateRequestDTO("Phones", 999L);
            when(categoryRepository.findById(999L))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> categoryService.createCategory(createRequest))
                    .isInstanceOf(java.util.NoSuchElementException.class);
            verify(categoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw NullPointerException when createRequestDTO is null")
        void shouldThrowExceptionWhenRequestDTOIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> categoryService.createCategory(null))
                    .isInstanceOf(NullPointerException.class);
            verify(categoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("updateCategory Tests")
    class UpdateCategoryTests {

        @Test
        @DisplayName("Should update category name successfully")
        void shouldUpdateCategoryNameSuccessfully() {
            // Arrange
            CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO(
                    1L,
                    JsonNullable.of("Updated Electronics"),
                    JsonNullable.undefined()
            );
            Category category = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();

            when(categoryRepository.findByIdForUpdate(1L))
                    .thenReturn(Optional.of(category));
            when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("Updated Electronics", 1L, null))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class)))
                    .thenReturn(category);
            when(categoryCacheService.getCategoryTree(1L))
                    .thenReturn("Updated Electronics");

            // Act
            CategoryResponseDTO result = categoryService.updateCategory(updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            verify(categoryRepository).save(argThat(c -> "Updated Electronics".equals(c.getName())));
            verify(categoryCacheService).evictAllCategoryTrees();
        }

        @Test
        @DisplayName("Should update category parent successfully")
        void shouldUpdateCategoryParentSuccessfully() {
            // Arrange
            CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO(
                    1L,
                    JsonNullable.undefined(),
                    JsonNullable.of(2L)
            );
            Category category = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();

            when(categoryRepository.findByIdForUpdate(1L))
                    .thenReturn(Optional.of(category));
            when(categoryRepository.getReferenceById(2L))
                    .thenReturn(parentCategory);
            when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("Electronics", 1L, 2L))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class)))
                    .thenReturn(category);
            when(categoryCacheService.getCategoryTree(1L))
                    .thenReturn("Devices > Electronics");

            // Act
            CategoryResponseDTO result = categoryService.updateCategory(updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.parentId()).isEqualTo(2L);
            verify(categoryRepository).save(argThat(c -> c.getParent() != null && c.getParent().getId() == 2L));
            verify(categoryCacheService).evictAllCategoryTrees();
        }

        @Test
        @DisplayName("Should remove parent category successfully")
        void shouldRemoveParentCategorySuccessfully() {
            // Arrange
            CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO(
                    1L,
                    JsonNullable.undefined(),
                    JsonNullable.of(null)
            );
            Category category = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(parentCategory)
                    .build();

                        when(categoryRepository.findByIdForUpdate(1L))
                    .thenReturn(Optional.of(category));
            when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("Electronics", 1L, null))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class)))
                    .thenReturn(category);
            when(categoryCacheService.getCategoryTree(1L))
                    .thenReturn("Electronics");

            // Act
            CategoryResponseDTO result = categoryService.updateCategory(updateRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.parentId()).isNull();
            verify(categoryRepository).save(argThat(c -> c.getParent() == null));
            verify(categoryCacheService).evictAllCategoryTrees();
        }

        @Test
        @DisplayName("Should update both name and parent successfully")
        void shouldUpdateBothNameAndParentSuccessfully() {
            // Arrange
            CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO(
                    1L,
                    JsonNullable.of("New Phones"),
                    JsonNullable.of(2L)
            );
            Category category = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();

            when(categoryRepository.findByIdForUpdate(1L))
                    .thenReturn(Optional.of(category));
            when(categoryRepository.getReferenceById(2L))
                    .thenReturn(parentCategory);
            when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("New Phones", 1L, 2L))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class)))
                    .thenReturn(category);
            when(categoryCacheService.getCategoryTree(1L))
                    .thenReturn("Devices > New Phones");

            // Act
            CategoryResponseDTO result = categoryService.updateCategory(updateRequest);

            // Assert
            assertThat(result).isNotNull();
            verify(categoryRepository).save(argThat(c ->
                    "New Phones".equals(c.getName()) && c.getParent() != null
            ));
            verify(categoryCacheService).evictAllCategoryTrees();
        }

        @Test
        @DisplayName("Should throw EntityExistsException when updated name already exists")
        void shouldThrowExceptionWhenUpdatedNameAlreadyExists() {
            // Arrange
            CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO(
                    1L,
                    JsonNullable.of("Devices"),
                    JsonNullable.undefined()
            );
            Category category = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();
            Category existingCategory = Category.builder()
                    .id(2L)
                    .name("Devices")
                    .parent(null)
                    .build();

                        when(categoryRepository.findByIdForUpdate(1L))
                    .thenReturn(Optional.of(category));
            when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("Devices", 1L, null))
                    .thenReturn(Optional.of(existingCategory));

            // Act & Assert
            assertThatThrownBy(() -> categoryService.updateCategory(updateRequest))
                    .isInstanceOf(EntityExistsException.class)
                    .hasMessageContaining("Devices")
                    .hasMessageContaining("already exists");
            verify(categoryCacheService, never()).evictAllCategoryTrees();
        }

        @Test
        @DisplayName("Should throw NoSuchElementException when category not found")
        void shouldThrowExceptionWhenCategoryNotFound() {
            // Arrange
            CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO(
                    999L,
                    JsonNullable.of("New Name"),
                    JsonNullable.undefined()
            );

            when(categoryRepository.findByIdForUpdate(999L))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> categoryService.updateCategory(updateRequest))
                    .isInstanceOf(java.util.NoSuchElementException.class);
            verify(categoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw NoSuchElementException when new parent category not found")
        void shouldThrowExceptionWhenNewParentCategoryNotFound() {
            // Arrange
            CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO(
                    1L,
                    JsonNullable.undefined(),
                    JsonNullable.of(999L)
            );
            Category category = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();

            when(categoryRepository.findByIdForUpdate(1L))
                    .thenReturn(Optional.of(category));
            when(categoryRepository.getReferenceById(999L))
                    .thenThrow(new jakarta.persistence.EntityNotFoundException());

            // Act & Assert
            assertThatThrownBy(() -> categoryService.updateCategory(updateRequest))
                    .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);
            verify(categoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw NullPointerException when updateRequestDTO is null")
        void shouldThrowExceptionWhenUpdateRequestDTOIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> categoryService.updateCategory(null))
                    .isInstanceOf(NullPointerException.class);
            verify(categoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should trim whitespace from updated name")
        void shouldTrimWhitespaceFromUpdatedName() {
            // Arrange
            CategoryUpdateRequestDTO updateRequest = new CategoryUpdateRequestDTO(
                    1L,
                    JsonNullable.of("  Updated Electronics  "),
                    JsonNullable.undefined()
            );
            Category category = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();

                        when(categoryRepository.findByIdForUpdate(1L))
                    .thenReturn(Optional.of(category));
            when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("Updated Electronics", 1L, null))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class)))
                    .thenReturn(category);
            when(categoryCacheService.getCategoryTree(1L))
                    .thenReturn("Updated Electronics");

            // Act
            categoryService.updateCategory(updateRequest);

            // Assert
            verify(categoryRepository).save(argThat(c -> "Updated Electronics".equals(c.getName())));
        }
    }

    @Nested
    @DisplayName("getCategoryList Tests")
    class GetCategoryListTests {

        @Test
        @DisplayName("Should return all categories successfully")
        void shouldReturnAllCategoriesSuccessfully() {
            // Arrange
            Category category1 = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();
            Category category2 = Category.builder()
                    .id(2L)
                    .name("Phones")
                    .parent(category1)
                    .build();

            Map<Long, String> categoryTrees = Map.of(
                    1L, "Electronics",
                    2L, "Electronics > Phones"
            );

            when(categoryCacheService.getCategoryTrees())
                    .thenReturn(categoryTrees);
            when(categoryRepository.findAll())
                    .thenReturn(List.of(category1, category2));

            // Act
            List<CategoryResponseDTO> result = categoryService.getCategoryList();

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(CategoryResponseDTO::id)
                    .containsExactly(1L, 2L);
            assertThat(result)
                    .extracting(CategoryResponseDTO::treeName)
                    .containsExactly("Electronics", "Electronics > Phones");
            assertThat(result)
                    .extracting(CategoryResponseDTO::parentId)
                    .containsExactly(null, 1L);
            verify(categoryCacheService).getCategoryTrees();
            verify(categoryRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no categories exist")
        void shouldReturnEmptyListWhenNoCategoriesExist() {
            // Arrange
            when(categoryCacheService.getCategoryTrees())
                    .thenReturn(Collections.emptyMap());
            when(categoryRepository.findAll())
                    .thenReturn(Collections.emptyList());

            // Act
            List<CategoryResponseDTO> result = categoryService.getCategoryList();

            // Assert
            assertThat(result).isEmpty();
            verify(categoryCacheService).getCategoryTrees();
            verify(categoryRepository).findAll();
        }

        @Test
        @DisplayName("Should handle single category successfully")
        void shouldHandleSingleCategorySuccessfully() {
            // Arrange
            Map<Long, String> categoryTrees = Map.of(1L, "Electronics");
            when(categoryCacheService.getCategoryTrees())
                    .thenReturn(categoryTrees);
            when(categoryRepository.findAll())
                    .thenReturn(List.of(testCategory));

            // Act
            List<CategoryResponseDTO> result = categoryService.getCategoryList();

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.getFirst().id()).isEqualTo(1L);
            assertThat(result.getFirst().treeName()).isEqualTo("Electronics");
            assertThat(result.getFirst().parentId()).isNull();
        }
    }

    @Nested
    @DisplayName("getCategory Tests")
    class GetCategoryTests {

        @Test
        @DisplayName("Should return category successfully")
        void shouldReturnCategorySuccessfully() {
            // Arrange
            when(categoryRepository.findById(1L))
                    .thenReturn(Optional.of(testCategory));
            when(categoryCacheService.getCategoryTree(1L))
                    .thenReturn("Electronics");

            // Act
            CategoryResponseDTO result = categoryService.getCategory(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.treeName()).isEqualTo("Electronics");
            assertThat(result.parentId()).isNull();
            verify(categoryRepository).findById(1L);
            verify(categoryCacheService).getCategoryTree(1L);
        }

        @Test
        @DisplayName("Should return subcategory with parent successfully")
        void shouldReturnSubcategoryWithParentSuccessfully() {
            // Arrange
            Category subcategory = Category.builder()
                    .id(3L)
                    .name("Phones")
                    .parent(parentCategory)
                    .build();

            when(categoryRepository.findById(3L))
                    .thenReturn(Optional.of(subcategory));
            when(categoryCacheService.getCategoryTree(3L))
                    .thenReturn("Devices > Phones");

            // Act
            CategoryResponseDTO result = categoryService.getCategory(3L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(3L);
            assertThat(result.treeName()).isEqualTo("Devices > Phones");
            assertThat(result.parentId()).isEqualTo(2L);
        }

        @Test
        @DisplayName("Should throw NoSuchElementException when category not found")
        void shouldThrowExceptionWhenCategoryNotFound() {
            // Arrange
            when(categoryRepository.findById(999L))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> categoryService.getCategory(999L))
                    .isInstanceOf(java.util.NoSuchElementException.class);
            verify(categoryRepository).findById(999L);
            verify(categoryCacheService, never()).getCategoryTree(any());
        }
    }

    @Nested
    @DisplayName("getCategoryTree Tests")
    class GetCategoryTreeTests {

        @Test
        @DisplayName("Should return category tree successfully")
        void shouldReturnCategoryTreeSuccessfully() {
            // Arrange
            Category root = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();
            Category child1 = Category.builder()
                    .id(2L)
                    .name("Phones")
                    .parent(root)
                    .build();
            Category child2 = Category.builder()
                    .id(3L)
                    .name("Laptops")
                    .parent(root)
                    .build();
            Category grandchild = Category.builder()
                    .id(4L)
                    .name("Smartphones")
                    .parent(child1)
                    .build();

            when(categoryRepository.findAllWithParent())
                    .thenReturn(List.of(root, child1, child2, grandchild));

            // Act
            List<NodeDTO> result = categoryService.getCategoryTree();

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.getFirst().id()).isEqualTo(1L);
            assertThat(result.getFirst().name()).isEqualTo("Electronics");
            assertThat(result.getFirst().childNodes()).hasSize(2);

            List<NodeDTO> children = result.getFirst().childNodes();
            assertThat(children)
                    .extracting(NodeDTO::name)
                    .containsExactlyInAnyOrder("Phones", "Laptops");

            NodeDTO phonesNode = children.stream()
                    .filter(child -> "Phones".equals(child.name()))
                    .findFirst()
                    .orElseThrow();
            assertThat(phonesNode.childNodes()).hasSize(1);
            assertThat(phonesNode.childNodes().getFirst().name()).isEqualTo("Smartphones");

            verify(categoryRepository).findAllWithParent();
        }

        @Test
        @DisplayName("Should return empty list when no categories exist")
        void shouldReturnEmptyListWhenNoCategoriesExist() {
            // Arrange
            when(categoryRepository.findAllWithParent())
                    .thenReturn(Collections.emptyList());

            // Act
            List<NodeDTO> result = categoryService.getCategoryTree();

            // Assert
            assertThat(result).isEmpty();
            verify(categoryRepository).findAllWithParent();
        }

        @Test
        @DisplayName("Should handle single root category without children")
        void shouldHandleSingleRootCategoryWithoutChildren() {
            // Arrange
            when(categoryRepository.findAllWithParent())
                    .thenReturn(List.of(testCategory));

            // Act
            List<NodeDTO> result = categoryService.getCategoryTree();

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.getFirst().id()).isEqualTo(1L);
            assertThat(result.getFirst().name()).isEqualTo("Electronics");
            assertThat(result.getFirst().childNodes()).isEmpty();
        }

        @Test
        @DisplayName("Should handle multiple root categories")
        void shouldHandleMultipleRootCategories() {
            // Arrange
            Category root1 = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();
            Category root2 = Category.builder()
                    .id(2L)
                    .name("Clothing")
                    .parent(null)
                    .build();

            when(categoryRepository.findAllWithParent())
                    .thenReturn(List.of(root1, root2));

            // Act
            List<NodeDTO> result = categoryService.getCategoryTree();

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(NodeDTO::name)
                    .containsExactlyInAnyOrder("Electronics", "Clothing");
            assertThat(result).allMatch(node -> node.childNodes().isEmpty());
        }

        @Test
        @DisplayName("Should handle deep hierarchy")
        void shouldHandleDeepHierarchy() {
            // Arrange
            Category level1 = Category.builder()
                    .id(1L)
                    .name("Level1")
                    .parent(null)
                    .build();
            Category level2 = Category.builder()
                    .id(2L)
                    .name("Level2")
                    .parent(level1)
                    .build();
            Category level3 = Category.builder()
                    .id(3L)
                    .name("Level3")
                    .parent(level2)
                    .build();

            when(categoryRepository.findAllWithParent())
                    .thenReturn(List.of(level1, level2, level3));

            // Act
            List<NodeDTO> result = categoryService.getCategoryTree();

            // Assert
            assertThat(result).hasSize(1);
            NodeDTO node1 = result.getFirst();
            assertThat(node1.name()).isEqualTo("Level1");
            assertThat(node1.childNodes()).hasSize(1);

            NodeDTO node2 = node1.childNodes().getFirst();
            assertThat(node2.name()).isEqualTo("Level2");
            assertThat(node2.childNodes()).hasSize(1);

            NodeDTO node3 = node2.childNodes().getFirst();
            assertThat(node3.name()).isEqualTo("Level3");
            assertThat(node3.childNodes()).isEmpty();
        }

        @Test
        @DisplayName("Should handle complex tree structure with multiple branches")
        void shouldHandleComplexTreeStructure() {
            // Arrange
            Category root = Category.builder()
                    .id(1L)
                    .name("Electronics")
                    .parent(null)
                    .build();
            Category child1 = Category.builder()
                    .id(2L)
                    .name("Mobile")
                    .parent(root)
                    .build();
            Category child2 = Category.builder()
                    .id(3L)
                    .name("Computer")
                    .parent(root)
                    .build();
            Category grandchild1 = Category.builder()
                    .id(4L)
                    .name("Smartphone")
                    .parent(child1)
                    .build();
            Category grandchild2 = Category.builder()
                    .id(5L)
                    .name("Tablet")
                    .parent(child1)
                    .build();
            Category grandchild3 = Category.builder()
                    .id(6L)
                    .name("Laptop")
                    .parent(child2)
                    .build();

            when(categoryRepository.findAllWithParent())
                    .thenReturn(List.of(root, child1, child2, grandchild1, grandchild2, grandchild3));

            // Act
            List<NodeDTO> result = categoryService.getCategoryTree();

            // Assert
            assertThat(result).hasSize(1);
            NodeDTO rootNode = result.getFirst();
            assertThat(rootNode.name()).isEqualTo("Electronics");
            assertThat(rootNode.childNodes()).hasSize(2);

            Map<String, NodeDTO> childMap = rootNode.childNodes().stream()
                    .collect(java.util.stream.Collectors.toMap(NodeDTO::name, n -> n));

            assertThat(childMap.get("Mobile").childNodes()).hasSize(2);
            assertThat(childMap.get("Computer").childNodes()).hasSize(1);
        }
    }
}
