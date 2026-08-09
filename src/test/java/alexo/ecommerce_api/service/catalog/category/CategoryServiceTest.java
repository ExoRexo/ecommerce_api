package alexo.ecommerce_api.service.catalog.category;

import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.repository.catalog.CategoryRepository;
import alexo.ecommerce_api.service.catalog.category.dto.CategoryResponseDTO;
import alexo.ecommerce_api.service.catalog.category.dto.create.CreateRequestDTO;
import alexo.ecommerce_api.service.catalog.category.dto.update.UpdateRequestDTO;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryCacheService categoryCacheService;
    @InjectMocks
    private CategoryService categoryService;

    // ── createCategory ──────────────────────────────────────────────────────────

    @Test
    void createCategory_rootCategory_returnsCategoryResponse() {
        CreateRequestDTO request = new CreateRequestDTO("Electronics And Gadgets", null);

        when(categoryRepository.findByNameAndParentId("Electronics And Gadgets", null)).thenReturn(Optional.empty());
        when(categoryCacheService.getCategoryTree(any())).thenReturn("Electronics And Gadgets");

        CategoryResponseDTO response = categoryService.createCategory(request);

        assertThat(response.treeName()).isEqualTo("Electronics And Gadgets");
        assertThat(response.parentId()).isNull();
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_withParent_setsParentAndReturnsParentId() {
        Category parent = new Category(10L, "Electronics And Gadgets", null);
        CreateRequestDTO request = new CreateRequestDTO("Mobile Phones Category", 10L);

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(parent));
        when(categoryRepository.findByNameAndParentId("Mobile Phones Category", 10L)).thenReturn(Optional.empty());
        when(categoryCacheService.getCategoryTree(any())).thenReturn("Electronics And Gadgets > Mobile Phones Category");

        CategoryResponseDTO response = categoryService.createCategory(request);

        assertThat(response.parentId()).isEqualTo(10L);
        assertThat(response.treeName()).isEqualTo("Electronics And Gadgets > Mobile Phones Category");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_duplicateRootCategory_throwsEntityExistsException() {
        Category existing = new Category(5L, "Electronics And Gadgets", null);
        CreateRequestDTO request = new CreateRequestDTO("Electronics And Gadgets", null);

        when(categoryRepository.findByNameAndParentId("Electronics And Gadgets", null)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("root category");
    }

    @Test
    void createCategory_duplicateChildCategory_throwsEntityExistsException() {
        Category parent = new Category(10L, "Electronics And Gadgets", null);
        Category existing = new Category(5L, "Mobile Phones Category", parent);
        CreateRequestDTO request = new CreateRequestDTO("Mobile Phones Category", 10L);

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(parent));
        when(categoryRepository.findByNameAndParentId("Mobile Phones Category", 10L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("parent category");
    }

    @Test
    void createCategory_parentNotFound_throwsNoSuchElementException() {
        CreateRequestDTO request = new CreateRequestDTO("Mobile Phones Category", 999L);

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void createCategory_nullRequest_throwsNullPointerException() {
        assertThatThrownBy(() -> categoryService.createCategory(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ── updateCategory ──────────────────────────────────────────────────────────

    @Test
    void updateCategory_changeName_updatesAndReturnsResponse() {
        Category category = new Category(1L, "Old Category Name X", null);
        UpdateRequestDTO request = new UpdateRequestDTO(1L, "New Category Name X", JsonNullable.undefined());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("New Category Name X", 1L, null)).thenReturn(Optional.empty());
        when(categoryCacheService.getCategoryTree(1L)).thenReturn("New Category Name X");

        CategoryResponseDTO response = categoryService.updateCategory(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.treeName()).isEqualTo("New Category Name X");
        verify(categoryRepository).save(category);
        verify(categoryCacheService).evictAllCategoryTrees();
    }

    @Test
    void updateCategory_clearParent_setsParentToNull() {
        Category parent = new Category(10L, "Electronics And Gadgets", null);
        Category category = new Category(1L, "Mobile Phones Category", parent);
        UpdateRequestDTO request = new UpdateRequestDTO(1L, null, JsonNullable.of(null));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("Mobile Phones Category", 1L, null)).thenReturn(Optional.empty());
        when(categoryCacheService.getCategoryTree(1L)).thenReturn("Mobile Phones Category");

        CategoryResponseDTO response = categoryService.updateCategory(request);

        assertThat(response.parentId()).isNull();
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategory_changeParent_updatesParentId() {
        Category oldParent = new Category(10L, "Electronics And Gadgets", null);
        Category newParent = new Category(20L, "Computing And Accessories", null);
        Category category = new Category(1L, "Laptop Category Name", oldParent);
        UpdateRequestDTO request = new UpdateRequestDTO(1L, null, JsonNullable.of(20L));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(20L)).thenReturn(Optional.of(newParent));
        when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("Laptop Category Name", 1L, 20L)).thenReturn(Optional.empty());
        when(categoryCacheService.getCategoryTree(1L)).thenReturn("Computing And Accessories > Laptop Category Name");

        CategoryResponseDTO response = categoryService.updateCategory(request);

        assertThat(response.parentId()).isEqualTo(20L);
        assertThat(response.treeName()).isEqualTo("Computing And Accessories > Laptop Category Name");
    }

    @Test
    void updateCategory_nameConflicts_throwsEntityExistsException() {
        Category category = new Category(1L, "Old Category Name X", null);
        Category existing = new Category(2L, "Conflicting Name XX", null);
        UpdateRequestDTO request = new UpdateRequestDTO(1L, "Conflicting Name XX", JsonNullable.undefined());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByNameAndIdIsNotAndParentIdIs("Conflicting Name XX", 1L, null)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> categoryService.updateCategory(request))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void updateCategory_categoryNotFound_throwsNoSuchElementException() {
        UpdateRequestDTO request = new UpdateRequestDTO(999L, "Some Category Name", JsonNullable.undefined());

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void updateCategory_nullRequest_throwsNullPointerException() {
        assertThatThrownBy(() -> categoryService.updateCategory(null))
                .isInstanceOf(NullPointerException.class);
    }
}
