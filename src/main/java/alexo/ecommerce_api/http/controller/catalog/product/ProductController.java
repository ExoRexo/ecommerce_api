package alexo.ecommerce_api.http.controller.catalog.product;

import alexo.ecommerce_api.dto.service.catalog.product.create.request.ProductCreateRequestDTO;
import alexo.ecommerce_api.dto.service.catalog.product.ProductResponseDTO;
import alexo.ecommerce_api.service.catalog.product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/catalog/product")
public class ProductController {
    private ProductService productService;

    @PostMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CATALOG_PRODUCT_CREATE')") todo
    public ProductResponseDTO createCategory(@Valid @RequestBody ProductCreateRequestDTO request) {
        return productService.createProduct(request);
    }
}
