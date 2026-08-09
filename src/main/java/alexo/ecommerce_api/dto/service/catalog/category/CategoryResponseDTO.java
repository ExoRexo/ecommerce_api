package alexo.ecommerce_api.dto.service.catalog.category;


public record CategoryResponseDTO(
        Long id,
        String treeName,
        Long parentId
) {
}
