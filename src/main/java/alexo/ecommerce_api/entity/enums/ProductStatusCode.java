package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Product status values stored in product_status_types.code.
 */
@Getter
@RequiredArgsConstructor
public enum ProductStatusCode implements EnumCode {
    ACTIVE("ACTIVE"),
    UNACTIVE("UNACTIVE");

    private final String code;
}
