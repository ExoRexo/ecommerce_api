package alexo.ecommerce_api.enums.entity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Product status values stored in product_status_types.code.
 */
@Getter
@AllArgsConstructor
public enum ProductStatusCode implements EnumCode {
    ACTIVE,
    UNACTIVE
}
