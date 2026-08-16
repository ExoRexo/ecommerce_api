package alexo.ecommerce_api.dto.service.internal.customer.cart.produt_list;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record CartProductListRequestDTO(
        @NotNull @Valid SortDTO sortDTO,
        @NotNull @Valid PaginationDTO paginationDTO
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
}
