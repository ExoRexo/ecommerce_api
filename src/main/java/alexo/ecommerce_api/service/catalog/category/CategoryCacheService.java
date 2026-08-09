package alexo.ecommerce_api.service.catalog.category;

import alexo.ecommerce_api.repository.catalog.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class CategoryCacheService {
    private CategoryRepository categoryRepository;

    @Cacheable(
            value = "catalog.category.categoryTree",
            key = "#categoryId"
    )
    public String getCategoryTree(Long categoryId) {
        return categoryRepository.findTreeName(Objects.requireNonNull(categoryId));
    }
}
