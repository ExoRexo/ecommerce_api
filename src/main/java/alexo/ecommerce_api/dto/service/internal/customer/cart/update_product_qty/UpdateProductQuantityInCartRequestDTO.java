package alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProductQuantityInCartRequestDTO(
        @NotNull
        Long productId,

        @NotNull
        @Min(0)
        Integer quantity
) {}
