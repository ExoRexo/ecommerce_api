package alexo.ecommerce_api.entity.enums;

import alexo.ecommerce_api.entity.common.EnumCode;
import alexo.ecommerce_api.entity.common.EnumDescription;
import alexo.ecommerce_api.entity.common.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Role values stored in roles.code.
 */
@Getter
@RequiredArgsConstructor
public enum RoleCode implements EnumCode, EnumLabel, EnumDescription {
    ADMIN("ADMIN", "Administrator", "Platform administrator with full access rights."),
    MANAGER("MANAGER", "Manager", "Operations manager with catalog and order management rights."),
    CUSTOMER("CUSTOMER", "Customer", "End user role for shopping and order placement.");

    private final String code;
    private final String label;
    private final String description;
}
