package alexo.ecommerce_api.unit.service.internal.customer.cart;

import alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty.UpdateProductQuantityInCartRequestDTO;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.customer.CartItemRepository;
import alexo.ecommerce_api.repository.customer.CustomerCartRepository;
import alexo.ecommerce_api.service.internal.customer.cart.CartService;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private CustomerCartRepository customerCartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldDeleteCartItemWhenQuantityIsZero() {
        when(authorizationService.getCurrentUserIdFromAuthentication()).thenReturn(7L);

        var response = cartService.updateProductQuantityInCart(
                new UpdateProductQuantityInCartRequestDTO(11L, 0)
        );

        assertThat(response.productId()).isEqualTo(11L);
        assertThat(response.quantity()).isZero();
        verify(cartItemRepository).deleteByCart_CustomerIdAndProduct_Id(7L, 11L);
    }

    @Test
    void shouldCreateCartItemWhenItDoesNotExist() {
        when(authorizationService.getCurrentUserIdFromAuthentication()).thenReturn(7L);
        when(cartItemRepository.findByCart_CustomerIdAndProduct_IdForUpdate(7L, 11L))
                .thenReturn(java.util.Optional.empty());
        when(customerCartRepository.getReferenceById(7L)).thenReturn(null);
        when(productRepository.getReferenceById(11L)).thenReturn(null);
        when(cartItemRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> {
                    var item = invocation.getArgument(0, alexo.ecommerce_api.entity.customer.cart.CartItem.class);
                    item.setProduct(alexo.ecommerce_api.entity.catalog.Product.builder().id(11L).build());
                    return item;
                });

        var response = cartService.updateProductQuantityInCart(
                new UpdateProductQuantityInCartRequestDTO(11L, 3)
        );

        assertThat(response.productId()).isEqualTo(11L);
        assertThat(response.quantity()).isEqualTo(3);
        verify(cartItemRepository).save(org.mockito.ArgumentMatchers.any());
    }
}
