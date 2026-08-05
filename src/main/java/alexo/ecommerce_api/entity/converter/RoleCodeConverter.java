package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.common.EnumCodeAttributeConverter;
import alexo.ecommerce_api.entity.enums.RoleCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link RoleCode}.
 */
@Converter
public class RoleCodeConverter extends EnumCodeAttributeConverter<RoleCode> {

    public RoleCodeConverter() {
        super(RoleCode.class);
    }
}
