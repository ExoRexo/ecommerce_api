package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.enums.contract.EnumCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Base JPA converter that persists enum codes and restores enums from those codes.
 *
 * @param <E> enum type implementing {@link EnumCode}
 */
@Converter
public abstract class AbstractEnumCodeAttributeConverter<E extends Enum<E> & EnumCode>
    implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    protected AbstractEnumCodeAttributeConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        return EnumCodeMapper.fromCode(enumClass, dbData);
    }
}
