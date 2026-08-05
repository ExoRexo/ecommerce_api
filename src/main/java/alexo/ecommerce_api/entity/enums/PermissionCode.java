package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Permission values stored in permissions.code.
 * Currently, intentionally empty and will be filled later.
 */
@Getter
@RequiredArgsConstructor
public enum PermissionCode implements EnumCode {
	;

	private final String code;
}
