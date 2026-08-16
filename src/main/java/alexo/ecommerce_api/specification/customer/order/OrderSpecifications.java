package alexo.ecommerce_api.specification.customer.order;

import alexo.ecommerce_api.dto.service.internal.customer.order.list.OrderListRequestDTO;
import alexo.ecommerce_api.entity.customer.order.CustomerOrder;
import jakarta.persistence.criteria.Predicate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderSpecifications {

    public static class OrderListSpecification {

        public static Specification<@NotNull CustomerOrder> getSpecification(OrderListRequestDTO.FiltersDTO filtersDTO) {
            Objects.requireNonNull(filtersDTO);

            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (filtersDTO.orderId() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("id"), filtersDTO.orderId())
                    );
                }

                if (filtersDTO.statusCode() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("statusType").get("code"), filtersDTO.statusCode())
                    );
                }

                if (filtersDTO.startDate() != null) {
                    predicates.add(
                            criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filtersDTO.startDate())
                    );
                }

                if (filtersDTO.endDate() != null) {
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filtersDTO.endDate())
                    );
                }

                if (filtersDTO.customerId() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("customer").get("userId"), filtersDTO.customerId())
                    );
                }

                return criteriaBuilder.and(predicates);
            };
        }
    }
}
