package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Product status values stored in product_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum ProductStatusCode implements EnumCode, EnumLabel, EnumDescription {
    ACTIVE("Активен", "Товар активен и доступен для продажи."),
    UNACTIVE("Неактивен", "Товар неактивен и скрыт с витрины.");

    private final String label;
    private final String description;
}
