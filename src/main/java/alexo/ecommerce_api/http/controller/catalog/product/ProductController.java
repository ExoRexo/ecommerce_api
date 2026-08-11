package alexo.ecommerce_api.http.controller.catalog.product;

import alexo.ecommerce_api.dto.service.catalog.product.create.request.ProductCreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.product.ProductResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.FiltersDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.PaginationDTO;
import alexo.ecommerce_api.dto.service.catalog.product.list.request.SortDTO;
import alexo.ecommerce_api.dto.service.catalog.product.update.request.ProductUpdateRequestDTO;
import alexo.ecommerce_api.enums.entity.ProductStatusCode;
import alexo.ecommerce_api.service.catalog.product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@AllArgsConstructor
@RestController
@RequestMapping("/api/catalog/product")
public class ProductController {
    private ProductService productService;

    @PostMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_PRODUCT_CREATE')") todo
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductCreateRequestDTO request) {
        return productService.createProduct(request);
    }

    @PutMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_PRODUCT_UPDATE')") todo
    public ProductResponseDTO updateProduct(@Valid @RequestBody ProductUpdateRequestDTO request) {
        return productService.updateProduct(request);
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_PRODUCT_READ_LIST')") todo
    public Page<@NotNull ProductResponseDTO> getProductList(
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
        return productService.getProductList(
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
        );
    }
}
