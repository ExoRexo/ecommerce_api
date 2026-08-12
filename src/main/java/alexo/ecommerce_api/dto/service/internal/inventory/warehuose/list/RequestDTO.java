package alexo.ecommerce_api.dto.service.internal.inventory.warehuose.list;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record RequestDTO(
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
            Long id,
            String name,
            String addressAddress,
            String addressMailIndex,
            String addressCountry,
            String addressCity
    ) {
    }
}
