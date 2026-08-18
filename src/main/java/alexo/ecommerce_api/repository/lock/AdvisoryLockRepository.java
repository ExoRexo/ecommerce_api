package alexo.ecommerce_api.repository.lock;

import alexo.ecommerce_api.contract.enums.EnumCode;
import alexo.ecommerce_api.entity.inventory.ProductWarehouseStock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisoryLockRepository extends JpaRepository<ProductWarehouseStock, Long> {

    @Query(
            value = """
                    select pg_advisory_xact_lock(
                        :#{#lockCode.advisoryLockKey}
                    )
                    """,
            nativeQuery = true
    )
    void acquireTransactionLock(@Param("lockCode") LockCode lockCode);

    @Getter
    @AllArgsConstructor
    enum LockCode implements EnumCode {
        INVENTORY_PRODUCT_WAREHOUSE_STOCK_MATRIX_LOCK_KEY(1L);

        private final Long advisoryLockKey;
    }
}
