package alexo.ecommerce_api.service.internal.customer.cart;

import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.produt_list.CartProductListRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.produt_list.CartProductListResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty.UpdateProductQuantityInCartRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty.UpdateProductQuantityInCartResponseDTO;
import alexo.ecommerce_api.entity.customer.cart.CartItem;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.customer.CartItemRepository;
import alexo.ecommerce_api.repository.customer.CustomerCartRepository;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
@Validated
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final AuthorizationService authorizationService;
    private final CustomerCartRepository customerCartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public UpdateProductQuantityInCartResponseDTO updateProductQuantityInCart(@Valid UpdateProductQuantityInCartRequestDTO requestDTO) {
        Assert.notNull(requestDTO, "requestDTO must be not null");

        Long productId = requestDTO.productId();
        Integer quantity = requestDTO.quantity();
        Long userId = authorizationService.getCurrentUserIdFromAuthentication();

        if (quantity == 0) {
            cartItemRepository.deleteByCart_CustomerIdAndProduct_Id(userId, productId);

            return new UpdateProductQuantityInCartResponseDTO(
                    productId,
                    0
            );
        }

        Optional<CartItem> cartItemOption = cartItemRepository.findByCart_CustomerIdAndProduct_IdForUpdate(userId, productId);

        CartItem cartItem = cartItemOption.orElseGet(() -> CartItem.builder()
                .cart(customerCartRepository.getReferenceById(userId))
                .product(productRepository.getReferenceById(productId))
                .build());

        cartItem.setQuantity(quantity);
        cartItem = cartItemRepository.save(cartItem);

        return new UpdateProductQuantityInCartResponseDTO(
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
    }

    public PageResponseDTO<CartProductListResponseDTO> getCartProductList(@Valid CartProductListRequestDTO request)  {
        Assert.notNull(request, "request must be not null");

        List<Sort.Order> orders = new ArrayList<>();

        orders.add(new Sort.Order(request.sortDTO().direction(), request.sortDTO().field()));

        PageRequest pageRequest = PageRequest.of(
                request.paginationDTO().page(),
                request.paginationDTO().size(),
                Sort.by(orders)
        );

        Page<@NotNull CartItem> cartItemsPage = cartItemRepository.findAll(pageRequest);

        return PageResponseDTO.from(cartItemsPage.map(cartItem -> new CartProductListResponseDTO(
                cartItem.getProduct().getId(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPriceRub()
        )));
    }

}
