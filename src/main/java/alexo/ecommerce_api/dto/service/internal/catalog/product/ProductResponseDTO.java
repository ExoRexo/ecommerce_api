package alexo.ecommerce_api.dto.service.internal.catalog.product;

import alexo.ecommerce_api.dto.service.internal.catalog.product.create.response.CategoryDTO;
import alexo.ecommerce_api.dto.service.internal.catalog.product.create.response.StatusTypeDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductResponseDTO(
                Long id,
                String name,
                String code,
                String description,
                BigDecimal priceRub,
                OffsetDateTime createdAt,
                StatusTypeDTO statusType,
                CategoryDTO category
) {
}
