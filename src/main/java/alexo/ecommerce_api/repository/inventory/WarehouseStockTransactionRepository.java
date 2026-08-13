package alexo.ecommerce_api.repository.inventory;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseStockTransactionRepository extends JpaRepository<WarehouseStockTransaction, Long> {

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
}

