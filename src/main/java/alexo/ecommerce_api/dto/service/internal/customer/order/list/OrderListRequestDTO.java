package alexo.ecommerce_api.dto.service.internal.customer.order.list;

import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;

public record OrderListRequestDTO(
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
            Long orderId,
            CustomerOrderStatusType.CustomerOrderStatusCode statusCode,
            Long customerId,
            OffsetDateTime startDate,
            OffsetDateTime endDate
    ) {

        public FiltersDTO {
            if (startDate != null
                    && endDate != null
                    && startDate.isAfter(endDate)) {

                throw new ValidationException(
                        "startDate must be before endDate"
                );
            }
        }
    }
}
