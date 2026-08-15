package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.order.OrderItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {
            "warehouseReservations"
    })
    @Query("""
    select o
    from OrderItem o
    where o.order.id = :id
""")
    List<OrderItem> findByOrderIdForCancelForUpdate(Long id);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {
            "warehouseReservations"
    })
    @Query("""
    select o
    from OrderItem o
    where o.order.id = :id
""")
    List<OrderItem> findByOrderIdForCompleteForUpdate(Long id);
}

