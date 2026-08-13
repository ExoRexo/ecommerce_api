package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.identity.UserStatusType;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link UserStatusType.UserStatusCode}.
 */
@Converter
public class UserStatusCodeConverter extends AbstractEnumCodeAttributeConverter<UserStatusType.UserStatusCode> {

    public UserStatusCodeConverter() {
        super(UserStatusType.UserStatusCode.class);
    }
}
