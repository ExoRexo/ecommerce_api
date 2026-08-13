package alexo.ecommerce_api.repository.inventory;

import alexo.ecommerce_api.entity.inventory.ProductWarehouseStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductWarehouseStockRepository extends JpaRepository<ProductWarehouseStock, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select s
    from ProductWarehouseStock s
    where s.product.id = :productId
      and s.warehouse.id = :warehouseId
""")
    Optional<ProductWarehouseStock> findByProductIdAndWarehouseIdForUpdate(Long productId, Long warehouseId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProductWarehouseStock p where p.warehouse.id = ?1 and p.product.id = ?2")
    Optional<ProductWarehouseStock> findByWarehouse_IdAndProduct_IdForUpdate(Long warehouseId, Long productId);

}

