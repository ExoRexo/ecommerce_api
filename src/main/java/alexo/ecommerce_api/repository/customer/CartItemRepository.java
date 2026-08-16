package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.cart.CartItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CartItem c where c.cart.customerId = ?1 and c.product.id = ?2")
    Optional<CartItem> findByCart_CustomerIdAndProduct_IdForUpdate(Long customerId, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {
            "product"
    })
    @Query("select c from CartItem c where c.cart.customerId = ?1 order by c.id asc")
    List<CartItem> findAllByCart_CustomerIdForUpdate(Long customerId);

    @Modifying
    @Query("delete from CartItem c where c.cart.customerId = ?1 and c.product.id = ?2")
    void deleteByCart_CustomerIdAndProduct_Id(Long customerId, Long id);


}

