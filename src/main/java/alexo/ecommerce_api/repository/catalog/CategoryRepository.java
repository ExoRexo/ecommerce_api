package alexo.ecommerce_api.repository.catalog;

import alexo.ecommerce_api.entity.catalog.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

