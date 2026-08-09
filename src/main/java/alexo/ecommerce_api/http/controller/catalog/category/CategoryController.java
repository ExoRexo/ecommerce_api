package alexo.ecommerce_api.http.controller.catalog.category;

import alexo.ecommerce_api.service.catalog.category.CategoryService;
import alexo.ecommerce_api.service.catalog.category.dto.CategoryResponseDTO;
import alexo.ecommerce_api.service.catalog.category.dto.create.CreateRequestDTO;
import alexo.ecommerce_api.service.catalog.category.dto.update.UpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
