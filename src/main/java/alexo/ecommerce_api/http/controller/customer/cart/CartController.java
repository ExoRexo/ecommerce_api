package alexo.ecommerce_api.http.controller.customer.cart;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty.UpdateProductQuantityInCartRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty.UpdateProductQuantityInCartResponseDTO;
import alexo.ecommerce_api.service.internal.customer.cart.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/customer/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<@NotNull ApiResponseDTO<UpdateProductQuantityInCartResponseDTO>> updateProductQuantityInCart(@Valid @RequestBody UpdateProductQuantityInCartRequestDTO requestDTO) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(cartService.updateProductQuantityInCart(requestDTO)));
    }
}
