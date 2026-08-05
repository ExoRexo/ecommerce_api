package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.common.EnumCodeAttributeConverter;
import alexo.ecommerce_api.entity.enums.UserStatusCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link UserStatusCode}.
 */
@Converter
public class UserStatusCodeConverter extends EnumCodeAttributeConverter<UserStatusCode> {

    public UserStatusCodeConverter() {
        super(UserStatusCode.class);
    }
}
