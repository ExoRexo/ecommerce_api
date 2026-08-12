package alexo.ecommerce_api.dto.service.internal.catalog.product.list.request;

import alexo.ecommerce_api.enums.entity.ProductStatusCode;

import java.math.BigDecimal;

public record FiltersDTO(
        Long id,
        String name,
        String description,
        BigDecimal priceRub,
        ProductStatusCode statusCode,
        Long categoryId,
        String code
) {
}
