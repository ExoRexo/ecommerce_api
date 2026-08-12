package alexo.ecommerce_api.specification.catalog.product;

import alexo.ecommerce_api.dto.service.internal.catalog.product.list.request.FiltersDTO;
import alexo.ecommerce_api.entity.catalog.Product;
import jakarta.persistence.criteria.Predicate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductSpecifications {
    public static class ListSpecification {
        public static Specification<@NotNull Product> getSpecification(FiltersDTO filtersDTO) {
            Objects.requireNonNull(filtersDTO);

            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (filtersDTO.id() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("id"), filtersDTO.id())
                    );
                }

                if (filtersDTO.name() != null && !filtersDTO.name().trim().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(root.get("name"), "%" + filtersDTO.name().trim() + "%")
                    );
                }

                if (filtersDTO.description() != null && !filtersDTO.description().trim().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(root.get("description"), "%" + filtersDTO.description().trim() + "%")
                    );
                }

                if (filtersDTO.priceRub() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("priceRub"), filtersDTO.priceRub())
                    );
                }

                if (filtersDTO.statusCode() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("statusType").get("code"), filtersDTO.statusCode())
                    );
                }

                if (filtersDTO.categoryId() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("category").get("id"), filtersDTO.categoryId())
                    );
                }

                if (filtersDTO.code() != null && !filtersDTO.code().trim().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("code"), filtersDTO.code().trim())
                    );
                }

                return criteriaBuilder.and(predicates);
            };
        }
    }
}
