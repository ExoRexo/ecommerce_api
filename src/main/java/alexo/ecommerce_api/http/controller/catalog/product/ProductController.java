package alexo.ecommerce_api.http.controller.catalog.product;

import alexo.ecommerce_api.dto.service.catalog.product.create.request.ProductCreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.product.ProductResponseDTO;
import alexo.ecommerce_api.dto.service.catalog.product.update.request.ProductUpdateRequestDTO;
import alexo.ecommerce_api.service.catalog.product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
