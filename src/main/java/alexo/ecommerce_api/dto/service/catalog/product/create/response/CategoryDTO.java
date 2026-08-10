package alexo.ecommerce_api.dto.service.catalog.product.create.response;

public record CategoryDTO(
        Long id,
        String treeName,
        Long parentId
) {
}
