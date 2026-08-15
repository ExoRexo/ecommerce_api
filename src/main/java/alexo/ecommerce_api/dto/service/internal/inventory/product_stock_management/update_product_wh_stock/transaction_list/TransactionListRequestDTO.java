package alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.transaction_list;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;

public record TransactionListRequestDTO(
        @NotNull @Valid SortDTO sortDTO,
        @NotNull @Valid PaginationDTO paginationDTO,
        @NotNull @Valid FiltersDTO filtersDTO
) {
    public record SortDTO(
            @NotNull
            String field,

            @NotNull
            Sort.Direction direction
    ) {
    }

    public record PaginationDTO(
            @NotNull
            Integer page,

            @NotNull
            @Max(50)
            Integer size
    ) {
    }

    public record FiltersDTO(
            Long transactionId,
            Integer oldQuantity,
            Integer newQuantity,
            Integer delta,
            String warehouseName,
            Long warehouseId,
            WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode purposeCode,
            Long userId,
            Long productId,
            OffsetDateTime createdAt
    ) {
    }
}
