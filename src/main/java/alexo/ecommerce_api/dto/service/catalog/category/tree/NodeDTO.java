package alexo.ecommerce_api.dto.service.catalog.category.tree;

import java.util.List;

public record NodeDTO(
        Long id,
        String name,
        List<NodeDTO> childNodes
) {
}
