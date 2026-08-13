package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.identity.Permission;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link Permission.PermissionCode}.
 */
@Converter
public class PermissionCodeConverter extends AbstractEnumCodeAttributeConverter<Permission.PermissionCode> {

    public PermissionCodeConverter() {
        super(Permission.PermissionCode.class);
    }
}
