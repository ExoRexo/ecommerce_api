package alexo.ecommerce_api.http.controller.catalog.category;

import alexo.ecommerce_api.service.catalog.category.CategoryService;
import alexo.ecommerce_api.service.catalog.category.dto.create.CreateRequestDTO;
import alexo.ecommerce_api.service.catalog.category.dto.create.CreateResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/catalog/category")
public class CategoryController {
    private CategoryService categoryService;

    @PostMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_CREATE')") todo
    public CreateResponseDTO createCategory(@Valid @RequestBody CreateRequestDTO request) {
        return categoryService.createCategory(request);
    }
}
