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
    ACTIVE("ACTIVE", "Active", "Product is active and available for sale."),
    UNACTIVE("UNACTIVE", "Inactive", "Product is inactive and hidden from sale.");

    private final String code;
    private final String label;
    private final String description;
}
