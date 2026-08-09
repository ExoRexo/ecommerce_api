package alexo.ecommerce_api.service.catalog.category.dto.create;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRequestDTO(
        @NotBlank
        @Size(min = 10, max = 100)
        String name,

        Long parentId
) {
}
