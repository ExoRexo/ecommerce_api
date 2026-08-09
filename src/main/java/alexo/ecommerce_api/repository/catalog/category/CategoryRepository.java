package alexo.ecommerce_api.repository.catalog.category;

import alexo.ecommerce_api.entity.catalog.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = """
    WITH RECURSIVE category_tree AS (
        SELECT
            c.id,
            c.name,
            c.parent_id,
            c.name::text AS tree_name,
            c.id AS requested_category_id
        FROM categories c
        WHERE c.id IN (:categoryIds)

        UNION ALL

        SELECT
            p.id,
            p.name,
            p.parent_id,
            p.name || ' > ' || ct.tree_name,
            ct.requested_category_id
        FROM categories p
        JOIN category_tree ct ON ct.parent_id = p.id
    )
    SELECT
        requested_category_id AS category_id,
        tree_name
    FROM category_tree
    WHERE parent_id IS NULL
    """, nativeQuery = true)
    List<CategoryTreeProjection> findTreeNames(
            @Param("categoryIds") List<Long> categoryIds
    );

    @Query(value = """
    WITH RECURSIVE category_tree AS (
        SELECT
            c.id,
            c.name,
            c.parent_id,
            c.name::text AS tree_name,
            c.id AS requested_category_id
        FROM categories c

        UNION ALL

        SELECT
            p.id,
            p.name,
            p.parent_id,
            p.name || ' > ' || ct.tree_name,
            ct.requested_category_id
        FROM categories p
        JOIN category_tree ct ON ct.parent_id = p.id
    )
    SELECT
        requested_category_id AS category_id,
        tree_name
    FROM category_tree
    WHERE parent_id IS NULL
    """, nativeQuery = true)
    List<CategoryTreeProjection> findTreeNames();

    @Query(value = """
        WITH RECURSIVE category_tree AS (
            SELECT
                c.id,
                c.name,
                c.parent_id,
                c.name::text AS tree_name
            FROM categories c
            WHERE c.id = :categoryId

            UNION ALL

            SELECT
                p.id,
                p.name,
                p.parent_id,
                p.name || ' > ' || ct.tree_name
            FROM categories p
            JOIN category_tree ct ON ct.parent_id = p.id
        )
        SELECT tree_name
        FROM category_tree
        WHERE parent_id IS NULL
        """, nativeQuery = true)
    String findTreeName(@Param("categoryId") Long categoryId);

    Optional<Category> findByNameAndIdIsNotAndParentIdIs(String name, Long id, Long parentId);

    @EntityGraph("parent")
    Optional<Category> findByNameAndParentId(String name, Long parentId);
}

