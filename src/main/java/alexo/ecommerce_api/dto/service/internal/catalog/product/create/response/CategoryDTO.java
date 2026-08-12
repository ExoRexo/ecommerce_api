package alexo.ecommerce_api.dto.service.internal.catalog.product.create.response;

public record CategoryDTO(
        Long id,
        String treeName,
        Long parentId
) {
}
