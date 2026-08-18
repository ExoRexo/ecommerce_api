package alexo.ecommerce_api.integration;

import alexo.ecommerce_api.dto.service.internal.identity.authentication.signup.request.UserSignupRequestDTO;
import alexo.ecommerce_api.entity.catalog.Product;
import alexo.ecommerce_api.entity.customer.Customer;
import alexo.ecommerce_api.entity.customer.cart.CartItem;
import alexo.ecommerce_api.entity.customer.cart.CustomerCart;
import alexo.ecommerce_api.entity.customer.wallet.CustomerWallet;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.repository.customer.CartItemRepository;
import alexo.ecommerce_api.repository.customer.CustomerCartRepository;
import alexo.ecommerce_api.repository.customer.CustomerRepository;
import alexo.ecommerce_api.repository.customer.CustomerWalletRepository;
import alexo.ecommerce_api.service.internal.identity.authentication.signup.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomerOrderFixture {

    private final SignupService signupService;
    private final CustomerRepository customerRepository;
    private final CustomerCartRepository cartRepository;
    private final CustomerWalletRepository walletRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CustomerOrderData createCustomerWithProduct(
            CatalogInventoryFixture.CatalogInventoryData catalogData,
            int cartQuantity
    ) {
        User user = signupService.createUser(
                new UserSignupRequestDTO(
                        "fixture-" + System.nanoTime() + "@example.com",
                        "Fixture",
                        "Customer",
                        "password123"
                ),
                null,
                null
        );

        Customer customer = customerRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("Fixture customer was not created"));
        CustomerCart cart = cartRepository.findById(customer.getUserId())
                .orElseThrow(() -> new IllegalStateException("Fixture cart was not created"));
        CustomerWallet wallet = walletRepository.findById(customer.getUserId())
                .orElseThrow(() -> new IllegalStateException("Fixture wallet was not created"));

        CartItem cartItem = cartItemRepository.save(CartItem.builder()
                .cart(cart)
                .product(catalogData.product())
                .quantity(cartQuantity)
                .build());

        return new CustomerOrderData(user, customer, cart, wallet, cartItem, catalogData.product());
    }

    public record CustomerOrderData(
            User user,
            Customer customer,
            CustomerCart cart,
            CustomerWallet wallet,
            CartItem cartItem,
            Product product
    ) {
    }
}
