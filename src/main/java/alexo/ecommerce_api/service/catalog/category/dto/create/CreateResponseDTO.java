package alexo.ecommerce_api.service.catalog.category.dto.create;


public record CreateResponseDTO(
        Long id,
        String treeName,
        Long parentId
) {
}
