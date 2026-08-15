package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.order.CustomerOrder;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {
            "statusType"
    })
    @Query("""
    select c from CustomerOrder c where c.id = ?1
""")
    Optional<CustomerOrder> findByIdForCancelForUpdate(Long id);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {
            "statusType"
    })
    @Query("""
    select c from CustomerOrder c where c.id = ?1
""")
    Optional<CustomerOrder> findByIdForCompleteForUpdate(Long id);
}

