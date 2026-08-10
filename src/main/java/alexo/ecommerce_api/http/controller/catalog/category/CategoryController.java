package alexo.ecommerce_api.http.controller.catalog.category;

import alexo.ecommerce_api.dto.service.catalog.category.tree.NodeDTO;
import alexo.ecommerce_api.service.catalog.category.CategoryService;
import alexo.ecommerce_api.dto.service.catalog.category.CategoryResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.category.create.CreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.category.update.UpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/catalog/category")
public class CategoryController {
    private CategoryService categoryService;

    @PostMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_CREATE')") todo
    public CategoryResponseDTO createCategory(@Valid @RequestBody CreateRequestDTO request) {
        return categoryService.createCategory(request);
    }

    @PutMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_UPDATE')") todo
    public CategoryResponseDTO updateCategory(@Valid @RequestBody UpdateRequestDTO request) {
        return categoryService.updateCategory(request);
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_READ_LIST')") todo
    public List<CategoryResponseDTO> getCategoryList() {
        return categoryService.getCategoryList();
    }

    @GetMapping("/tree")
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_READ_TREE')") todo
    public List<NodeDTO> getCategoryTree() {
        return categoryService.getCategoryTree();
    }

    @GetMapping("/{categoryId}")
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_READ_CONCRETE')") todo
    public CategoryResponseDTO getCategory(@PathVariable Long categoryId) {
        return categoryService.getCategory(categoryId);
    }
}
