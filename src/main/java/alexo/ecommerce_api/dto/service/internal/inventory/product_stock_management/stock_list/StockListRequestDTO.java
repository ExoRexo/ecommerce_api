package alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.stock_list;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record StockListRequestDTO(
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
            Long physicalQuantity,
            Integer reservedQuantity,
            Integer freeQuantity,
            String warehouseName,
            Long warehouseId,
            Long productId
    ) {
    }
}
