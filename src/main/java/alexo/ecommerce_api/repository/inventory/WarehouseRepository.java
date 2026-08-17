package alexo.ecommerce_api.repository.inventory;

import alexo.ecommerce_api.entity.inventory.Warehouse;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {
            "address"
    })
    @Query("select w from Warehouse w where w.id = ?1")
    Optional<Warehouse> findByIdForUpdate(Long id);


    @Override
    @EntityGraph(attributePaths = {
            "address",
    })
    @NotNull
    Page<@NotNull Warehouse> findAll(@NotNull Specification<@NotNull Warehouse> specification, @NotNull Pageable pageable);
}

