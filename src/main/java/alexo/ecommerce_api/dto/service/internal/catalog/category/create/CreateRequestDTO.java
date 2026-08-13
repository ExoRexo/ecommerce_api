package alexo.ecommerce_api.dto.service.internal.catalog.category.create;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRequestDTO(
        @NotBlank
        @Size(min = 2, max = 100)
        String name,

        Long parentId
) {
}
