package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.enums.entity.RoleCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link RoleCode}.
 */
@Converter
public class RoleCodeConverter extends AbstractEnumCodeAttributeConverter<RoleCode> {

    public RoleCodeConverter() {
        super(RoleCode.class);
    }
}
