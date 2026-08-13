package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.cart.CartItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CartItem c where c.cart.customerId = ?1 and c.product.id = ?2")
    Optional<CartItem> findByCart_CustomerIdAndProduct_IdForUpdate(Long customerId, Long id);

    @Modifying
    @Query("delete from CartItem c where c.cart.customerId = ?1 and c.product.id = ?2")
    void deleteByCart_CustomerIdAndProduct_Id(Long customerId, Long id);


}

