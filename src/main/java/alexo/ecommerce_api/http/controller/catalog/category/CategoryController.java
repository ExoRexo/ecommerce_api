package alexo.ecommerce_api.http.controller.catalog.category;

import alexo.ecommerce_api.dto.service.catalog.category.tree.NodeDTO;
import alexo.ecommerce_api.service.catalog.category.CategoryService;
import alexo.ecommerce_api.dto.service.catalog.category.CategoryResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.category.create.CreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.category.update.UpdateRequestDTO;
import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/catalog/category")
public class CategoryController {
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_CREATE')")
    public ResponseEntity<@NotNull ApiResponseDTO<CategoryResponseDTO>> createCategory(@Valid @RequestBody CreateRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(categoryService.createCategory(request)));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_UPDATE')")
    public ResponseEntity<@NotNull ApiResponseDTO<CategoryResponseDTO>> updateCategory(@Valid @RequestBody UpdateRequestDTO request) {
        return ResponseEntity.ok(ApiResponseDTO.success(categoryService.updateCategory(request)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_READ_LIST')")
    public ResponseEntity<@NotNull ApiResponseDTO<List<CategoryResponseDTO>>> getCategoryList() {
        return ResponseEntity.ok(ApiResponseDTO.success(categoryService.getCategoryList()));
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_READ_TREE')")
    public ResponseEntity<@NotNull ApiResponseDTO<List<NodeDTO>>> getCategoryTree() {
        return ResponseEntity.ok(ApiResponseDTO.success(categoryService.getCategoryTree()));
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_CATEGORY_READ_CONCRETE')")
    public ResponseEntity<@NotNull ApiResponseDTO<CategoryResponseDTO>> getCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponseDTO.success(categoryService.getCategory(categoryId)));
    }
}
