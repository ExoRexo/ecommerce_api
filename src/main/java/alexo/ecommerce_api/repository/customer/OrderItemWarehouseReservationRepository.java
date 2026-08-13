package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.order.OrderItemReservationStatusType;
import alexo.ecommerce_api.entity.customer.order.OrderItemWarehouseReservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemWarehouseReservationRepository extends JpaRepository<OrderItemWarehouseReservation, Long> {
    @Query("""
            select (count(o) > 0) from OrderItemWarehouseReservation o
            where o.orderItem.id = ?1 and o.warehouse.id = ?2 and o.statusType.code = ?3""")
    boolean existsByOrderItem_IdAndWarehouse_IdAndStatusType_Code(Long id, Long warehouseId, OrderItemReservationStatusType.OrderItemReservationStatusCode code);

    @EntityGraph(attributePaths = {
            "orderItem",
            "warehouse.address",
            "statusType"
    })
    @Query("""
select o from OrderItemWarehouseReservation o where o.id = :id
""")
    Optional<OrderItemWarehouseReservation> findByIdForItemReservationResponse(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {
            "statusType",
            "orderItem"
    })
    @Query("""
select o from OrderItemWarehouseReservation o where o.id = :id
""")
    Optional<OrderItemWarehouseReservation> findByIdForCancelForUpdate(Long id);

}

