package alexo.ecommerce_api.repository.catalog;

import alexo.ecommerce_api.entity.catalog.ProductStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStatusTypeRepository extends JpaRepository<ProductStatusType, Short> {
}

