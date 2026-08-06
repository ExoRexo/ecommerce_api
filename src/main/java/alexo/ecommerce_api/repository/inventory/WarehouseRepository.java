package alexo.ecommerce_api.repository.inventory;

import alexo.ecommerce_api.entity.inventory.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}

