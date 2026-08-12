package alexo.ecommerce_api.repository.inventory;

import alexo.ecommerce_api.entity.inventory.Warehouse;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    @Query("select w from Warehouse w where w.id = ?1")
    @EntityGraph(attributePaths = {
            "address"
    })
    Optional<Warehouse> findByIdForUpdate(Long id);


    @Override
    @EntityGraph(attributePaths = {
            "address",
    })
    @NotNull
    Page<@NotNull Warehouse> findAll(@NotNull Specification<@NotNull Warehouse> specification, @NotNull Pageable pageable);
}

