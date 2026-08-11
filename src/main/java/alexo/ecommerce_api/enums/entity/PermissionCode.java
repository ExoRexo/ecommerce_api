package alexo.ecommerce_api.enums.entity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Permission values stored in permissions.code.
 * Currently, intentionally empty and will be filled later.
 */
@Getter
@RequiredArgsConstructor
public enum PermissionCode implements EnumCode {
    CATALOG_PRODUCT_CREATE,
    CATALOG_PRODUCT_READ_LIST,
    CATALOG_PRODUCT_READ_STATUS_TYPES,
    CATALOG_PRODUCT_UPDATE,
    CATALOG_PRODUCT_UPDATE_PRICE_RUB,

	CATALOG_CATEGORY_CREATE,
	CATALOG_CATEGORY_UPDATE,
	CATALOG_CATEGORY_READ_LIST,
	CATALOG_CATEGORY_READ_TREE,
	CATALOG_CATEGORY_READ_CONCRETE


}
