package alexo.ecommerce_api.specification.inventory.warehouse;

import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.list.RequestDTO;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import jakarta.persistence.criteria.Predicate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WarehouseSpecifications {
    public static class ListSpecification {

        public static Specification<@NotNull Warehouse> getSpecification(RequestDTO.FiltersDTO filtersDTO) {
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

                if (filtersDTO.addressAddress() != null && !filtersDTO.addressAddress().trim().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(root.get("address").get("address"), "%" + filtersDTO.addressAddress().trim() + "%")
                    );
                }

                if (filtersDTO.addressMailIndex() != null && !filtersDTO.addressMailIndex().trim().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(root.get("address").get("mailIndex"), "%" + filtersDTO.addressMailIndex().trim() + "%")
                    );
                }

                if (filtersDTO.addressCountry() != null && !filtersDTO.addressCountry().trim().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(root.get("address").get("country"), "%" + filtersDTO.addressCountry().trim() + "%")
                    );
                }

                if (filtersDTO.addressCity() != null && !filtersDTO.addressCity().trim().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(root.get("address").get("city"), "%" + filtersDTO.addressCity().trim() + "%")
                    );
                }

                return criteriaBuilder.and(predicates);
            };
        }
    }
}
