package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.identity.Role;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link Role.RoleCode}.
 */
@Converter
public class RoleCodeConverter extends AbstractEnumCodeAttributeConverter<Role.RoleCode> {

    public RoleCodeConverter() {
        super(Role.RoleCode.class);
    }
}
