package alexo.ecommerce_api.service.catalog.category.dto;


public record CategoryResponseDTO(
        Long id,
        String treeName,
        Long parentId
) {
}
