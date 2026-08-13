package alexo.ecommerce_api.dto.service.internal.catalog.product.create.response;

import alexo.ecommerce_api.entity.catalog.ProductStatusType;

public record StatusTypeDTO(
        ProductStatusType.ProductStatusCode code,
        String label,
        String description
) {
}
