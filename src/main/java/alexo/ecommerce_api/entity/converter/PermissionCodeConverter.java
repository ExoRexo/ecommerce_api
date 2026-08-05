package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.common.EnumCodeAttributeConverter;
import alexo.ecommerce_api.entity.enums.PermissionCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link PermissionCode}.
 */
@Converter
public class PermissionCodeConverter extends EnumCodeAttributeConverter<PermissionCode> {

    public PermissionCodeConverter() {
        super(PermissionCode.class);
    }
}
