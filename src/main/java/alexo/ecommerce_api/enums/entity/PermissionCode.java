package alexo.ecommerce_api.enums.entity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import alexo.ecommerce_api.contract.enums.EnumDescription;
import alexo.ecommerce_api.contract.enums.EnumLabel;
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
