package alexo.ecommerce_api.repository.inventory;

import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransaction;
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
public interface WarehouseStockTransactionRepository extends JpaRepository<WarehouseStockTransaction, Long>, JpaSpecificationExecutor<WarehouseStockTransaction> {

    @EntityGraph(attributePaths = {
            "product",
            "warehouse.address",
            "purposeType",
            "user"
    })
    @Query("""
select t from WarehouseStockTransaction t where t.id = :id
""")
    Optional<WarehouseStockTransaction> findByIdForStockManagementResponse(Long id);

    @Override
    @EntityGraph(attributePaths = {
            "product",
            "warehouse.address",
            "purposeType",
    })
    @NotNull
    Page<@NotNull WarehouseStockTransaction> findAll(@NotNull Specification<@NotNull WarehouseStockTransaction> specification, @NotNull Pageable pageable);
}

