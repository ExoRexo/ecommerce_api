package alexo.ecommerce_api.entity.enums.contract;

/**
 * Marker contract for enums that have a stable database code.
 */
public interface EnumCode {

    /**
     * @return string code stored in the dictionary table.
     */
    default String getCode() {
        if (this instanceof Enum<?> enumValue) {
            return enumValue.name();
        }
        throw new IllegalStateException("EnumCode must be implemented by enum types");
    }
}
