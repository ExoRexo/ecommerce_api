package alexo.ecommerce_api.http.controller.customer.cart;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.produt_list.CartProductListRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.produt_list.CartProductListResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty.UpdateProductQuantityInCartRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty.UpdateProductQuantityInCartResponseDTO;
import alexo.ecommerce_api.service.internal.customer.cart.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/products-list")
    public ResponseEntity<@NotNull ApiResponseDTO<PageResponseDTO<CartProductListResponseDTO>>> getCartProductList(
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size
    ) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(cartService.getCartProductList(
                        new CartProductListRequestDTO(
                                new CartProductListRequestDTO.SortDTO(
                                        sortField,
                                        sortDirection
                                ),
                                new CartProductListRequestDTO.PaginationDTO(
                                        page,
                                        size
                                )
                        )
                )));
    }
}
