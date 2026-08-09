package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.enums.entity.UserStatusCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link UserStatusCode}.
 */
@Converter
public class UserStatusCodeConverter extends AbstractEnumCodeAttributeConverter<UserStatusCode> {

    public UserStatusCodeConverter() {
        super(UserStatusCode.class);
    }
}
