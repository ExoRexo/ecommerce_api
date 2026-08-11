package alexo.ecommerce_api.http.controller.catalog.product;

import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.product.ProductResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.request.ProductCreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.product.create.response.StatusTypeDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.FiltersDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.PaginationDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.SortDTO;
import alexo.ecommerce_api.dto.service.catalog.product.update.request.ProductUpdateRequestDTO;
import alexo.ecommerce_api.enums.entity.ProductStatusCode;
import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.service.catalog.product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/catalog/product")
public class ProductController {
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_CATALOG_PRODUCT_CREATE')")
    public ResponseEntity<@NotNull ApiResponseDTO<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductCreateRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(productService.createProduct(request)));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_CATALOG_PRODUCT_UPDATE')")
    public ResponseEntity<@NotNull ApiResponseDTO<ProductResponseDTO>> updateProduct(@Valid @RequestBody ProductUpdateRequestDTO request) {
        return ResponseEntity.ok(ApiResponseDTO.success(productService.updateProduct(request)));
    }

    @GetMapping("/status-types")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_CATALOG_PRODUCT_READ_STATUS_TYPES')")
    public ResponseEntity<@NotNull ApiResponseDTO<List<StatusTypeDTO>>> getProductStatusTypes() {
        return ResponseEntity.ok(ApiResponseDTO.success(productService.getProductStatusList()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_CATALOG_PRODUCT_READ_LIST')")
    public ResponseEntity<@NotNull ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> getProductList(
            @RequestParam(name = "sortField", defaultValue = "createdAt") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "priceRub", required = false) BigDecimal priceRub,
            @RequestParam(name = "statusCode", required = false) ProductStatusCode statusCode,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size
    ) {
        return ResponseEntity.ok(ApiResponseDTO.success(productService.getProductList(
                new FiltersDTO(
                    id,
                    name,
                    description,
                    priceRub,
                    statusCode,
                    categoryId,
                    code
                ),
                new SortDTO(
                        sortField,
                        sortDirection
                ),
                new PaginationDTO(
                        page,
                        size
                )
            )));
    }
}
