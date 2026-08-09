package alexo.ecommerce_api.cache.catalog.category;

import alexo.ecommerce_api.repository.catalog.category.CategoryRepository;
import alexo.ecommerce_api.repository.catalog.category.CategoryTreeProjection;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class CategoryCacheService {

    private CategoryRepository categoryRepository;
    private CacheManager cacheManager;
    private static final String CACHE_KEY = "catalog.category.categoryTree";

    /**
     * @param categoryId category pk
     * @return string of category tree name
     */
    @Cacheable(
            value = CACHE_KEY,
            key = "#categoryId"
    )
    public @NotNull String getCategoryTree(Long categoryId) {
        return categoryRepository.findTreeName(
                Objects.requireNonNull(categoryId)
        );
    }

    /**
     * @return hashmap <category pk, category tree name>
     */
    public @NotNull Map<Long, String> getCategoryTrees() {
        Cache cache = Objects.requireNonNull(cacheManager.getCache(
                CACHE_KEY
        ));

        Map<Long, String> result = new HashMap<>();
        List<CategoryTreeProjection> trees = categoryRepository.findTreeNames();

        for (CategoryTreeProjection tree : trees) {

            result.put(
                    tree.getCategoryId(),
                    tree.getTreeName()
            );

            String treeName = cache.get(
                    tree.getCategoryId(),
                    String.class
            );

            if (treeName != null) {
                cache.put(
                        tree.getCategoryId(),
                        tree.getTreeName()
                );
            }

        }

        return result;
    }

    /**
     * @param categoryIds category pks
     * @return hashmap<category pk, category tree name>
     */
    public @NotNull Map<Long, String> getCategoryTrees(List<Long> categoryIds) {
        Objects.requireNonNull(categoryIds);

        Cache cache = Objects.requireNonNull(cacheManager.getCache(
            CACHE_KEY
        ));

        Map<Long, String> result = new HashMap<>();
        List<Long> missingIds = new ArrayList<>();

        for (Long categoryId : categoryIds) {

            String treeName = cache.get(
                    categoryId,
                    String.class
            );

            if (treeName != null) {
                result.put(categoryId, treeName);
            } else {
                missingIds.add(categoryId);
            }
        }

        if (!missingIds.isEmpty()) {

            List<CategoryTreeProjection> trees =
                    categoryRepository.findTreeNames(missingIds);

            for (CategoryTreeProjection tree : trees) {

                result.put(
                        tree.getCategoryId(),
                        tree.getTreeName()
                );

                cache.put(
                        tree.getCategoryId(),
                        tree.getTreeName()
                );
            }
        }

        return result;
    }

    @CacheEvict(
            value = CACHE_KEY,
            allEntries = true
    )
    public void evictAllCategoryTrees() {
    }
}