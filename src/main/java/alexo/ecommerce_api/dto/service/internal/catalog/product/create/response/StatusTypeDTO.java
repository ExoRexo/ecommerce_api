package alexo.ecommerce_api.dto.service.internal.catalog.product.create.response;

import alexo.ecommerce_api.enums.entity.ProductStatusCode;

public record StatusTypeDTO(
        ProductStatusCode code,
        String label,
        String description
) {
}
