package alexo.ecommerce_api.repository.catalog;

import alexo.ecommerce_api.entity.catalog.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByNameAndCategory_Id(String name, Long id);
    boolean existsByIdNotAndNameAndCategory_Id(Long id, String name, Long categoryId);

    @EntityGraph(attributePaths = {
            "statusType",
            "category.parent"
    })
    @Query("""
select p from Product p where p.id = :id
""")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);
}

