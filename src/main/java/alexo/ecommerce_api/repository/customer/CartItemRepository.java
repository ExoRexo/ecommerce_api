package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

