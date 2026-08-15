package alexo.ecommerce_api.specification.inventory.product_stock_management;

import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.transaction_list.TransactionListRequestDTO;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransaction;
import jakarta.persistence.criteria.Predicate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductStockManagementSpecifications {
    public static class TransactionListSpecification {

        public static Specification<@NotNull WarehouseStockTransaction> getSpecification(TransactionListRequestDTO.FiltersDTO filtersDTO) {
            Objects.requireNonNull(filtersDTO);

            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (filtersDTO.transactionId() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("id"), filtersDTO.transactionId())
                    );
                }

                if (filtersDTO.oldQuantity() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("oldQuantity"), filtersDTO.oldQuantity())
                    );
                }

                if (filtersDTO.newQuantity() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("newQuantity"), filtersDTO.newQuantity())
                    );
                }

                if (filtersDTO.delta() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("delta"), filtersDTO.delta())
                    );
                }

                if (filtersDTO.warehouseName() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("warehouse").get("name"), filtersDTO.warehouseName())
                    );
                }

                if (filtersDTO.warehouseId() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("warehouse").get("id"), filtersDTO.warehouseId())
                    );
                }

                if (filtersDTO.purposeCode() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("purposeType").get("code"), filtersDTO.purposeCode())
                    );
                }

                if (filtersDTO.userId() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("user").get("id"), filtersDTO.userId())
                    );
                }

                if (filtersDTO.productId() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("product").get("id"), filtersDTO.productId())
                    );
                }

                if (filtersDTO.createdAt() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.<OffsetDateTime>get("createdAt"), filtersDTO.createdAt())
                    );
                }

                return criteriaBuilder.and(predicates);
            };
        }
    }
}
