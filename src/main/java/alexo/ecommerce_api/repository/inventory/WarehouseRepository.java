package alexo.ecommerce_api.repository.inventory;

import alexo.ecommerce_api.entity.inventory.Warehouse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    @Query("select w from Warehouse w where w.id = ?1")
    @EntityGraph(attributePaths = {
            "address"
    })
    Optional<Warehouse> findByIdForUpdate(Long id);


//    findByIdForUpdate

}

