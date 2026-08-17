package alexo.ecommerce_api.repository.inventory;

import alexo.ecommerce_api.entity.inventory.ProductWarehouseStock;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductWarehouseStockRepository extends JpaRepository<ProductWarehouseStock, Long>, JpaSpecificationExecutor<ProductWarehouseStock> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select s
    from ProductWarehouseStock s
    where s.product.id = :productId
      and s.warehouse.id = :warehouseId
""")
    Optional<ProductWarehouseStock> findByProductIdAndWarehouseIdForUpdate(Long productId, Long warehouseId);

    @Override
    @EntityGraph(attributePaths = {
            "product",
            "warehouse.address",
    })
    @NotNull
    Page<@NotNull ProductWarehouseStock> findAll(@NotNull Specification<@NotNull ProductWarehouseStock> specification, @NotNull Pageable pageable);

}

