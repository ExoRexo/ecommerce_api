package alexo.ecommerce_api.dto.service.internal.customer.cart.produt_list;

import java.math.BigDecimal;

public record CartProductListResponseDTO(
        Long productId,
        Integer quantity,
        BigDecimal priceRubForUnit
) {}
