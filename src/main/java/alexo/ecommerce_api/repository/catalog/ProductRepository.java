package alexo.ecommerce_api.repository.catalog;

import alexo.ecommerce_api.entity.catalog.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
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

    @Override
    @EntityGraph(attributePaths = {
            "statusType",
            "category.parent"
    })
    Page<@NotNull Product> findAll(@NotNull Specification<@NotNull Product> specification, @NotNull Pageable pageable);
}

