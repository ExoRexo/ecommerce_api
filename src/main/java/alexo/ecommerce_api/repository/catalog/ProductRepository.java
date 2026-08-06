package alexo.ecommerce_api.repository.catalog;

import alexo.ecommerce_api.entity.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

