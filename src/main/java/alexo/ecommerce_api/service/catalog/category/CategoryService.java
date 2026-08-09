package alexo.ecommerce_api.service.catalog.category;

import alexo.ecommerce_api.entity.catalog.Category;
import alexo.ecommerce_api.repository.catalog.CategoryRepository;
import alexo.ecommerce_api.service.catalog.category.dto.create.CreateRequestDTO;
import alexo.ecommerce_api.service.catalog.category.dto.create.CreateResponseDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    /**
     * @param createRequestDTO category create request
     * @return category create response
     */
    @Transactional
    public CreateResponseDTO createCategory(CreateRequestDTO createRequestDTO) {
        Objects.requireNonNull(createRequestDTO);

        Long parentId = createRequestDTO.parentId();

        Category parent = null;
        if (parentId != null) {
            parent = categoryRepository.findById(parentId).orElseThrow();
        }

        String categoryName = createRequestDTO.name().trim();
        if (categoryRepository.existsByName(categoryName)) {
            throw new EntityExistsException("category with name [" + categoryName + "] is already exists");
        }

        if (categoryRepository.existsByNameAndParentId(categoryName, parentId)) {
            throw new EntityExistsException("category with name [" + categoryName + "] and parentId [" + parentId +  "] is already exists");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParent(parent);
        categoryRepository.save(category);

        return new CreateResponseDTO(
                category.getId(),
                category.getTreeName(),
                parentId
        );
    }

}
