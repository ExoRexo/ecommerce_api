package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Permission values stored in permissions.code.
 * Currently, intentionally empty and will be filled later.
 */
@Getter
@RequiredArgsConstructor
public enum PermissionCode implements EnumCode, EnumLabel, EnumDescription {
	;

	private final String label;
	private final String description;
}
