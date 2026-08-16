package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.order.CustomerOrder;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {

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

    @Override
    @EntityGraph(attributePaths = {
            "statusType",
    })
    @NotNull
    Page<@NotNull CustomerOrder> findAll(@NotNull Specification<@NotNull CustomerOrder> specification, @NotNull Pageable pageable);

    @EntityGraph(attributePaths = {
            "statusType",
            "items",
            "items.product",
            "items.warehouseReservations.statusType",
    })
    @Query("""
    select c from CustomerOrder c where c.id = ?1
""")
    Optional<CustomerOrder> findByIdForDetails(Long id);
}

