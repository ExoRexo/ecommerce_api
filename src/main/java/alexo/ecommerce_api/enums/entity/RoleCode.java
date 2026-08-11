package alexo.ecommerce_api.enums.entity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Role values stored in roles.code.
 */
@Getter
@RequiredArgsConstructor
public enum RoleCode implements EnumCode {
    ADMIN,
    MANAGER,
    CUSTOMER
}
