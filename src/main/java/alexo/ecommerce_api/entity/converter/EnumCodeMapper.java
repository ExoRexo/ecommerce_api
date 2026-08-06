package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.enums.contract.EnumCode;

/**
 * Utility for converting dictionary codes into strongly typed enum values.
 */
public final class EnumCodeMapper {

    private EnumCodeMapper() {
    }

    /**
     * Converts a string database code to an enum implementing {@link EnumCode}.
     *
     * @param enumClass enum type
     * @param code raw code from database
     * @param <E> enum generic type
     * @return matching enum constant
     * @throws IllegalArgumentException when code is unknown
     */
    public static <E extends Enum<E> & EnumCode> E fromCode(Class<E> enumClass, String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.getCode().equalsIgnoreCase(code)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException(
            "Unknown code '" + code + "' for enum " + enumClass.getSimpleName()
        );
    }
}
