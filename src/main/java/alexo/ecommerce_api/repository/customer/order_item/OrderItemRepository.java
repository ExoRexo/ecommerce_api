package alexo.ecommerce_api.repository.customer.order_item;

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
    order by o.id asc
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
    order by o.id asc
""")
    List<OrderItem> findByOrderIdForCompleteForUpdate(Long id);

    @Query("""
    select SUM(o.priceTotalRub) as priceTotalRubSum, o.order.id as orderId
    from OrderItem o
    where o.order.id = :id
    group by o.order.id
""")
    Optional<TotalAmountByOrderIdProjection> findTotalAmountByOrderId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select o
    from OrderItem o
    where o.id = :id
""")
    Optional<OrderItem> findByIdForUpdate(Long id);
}

