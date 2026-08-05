package alexo.ecommerce_api.entity.common;

/**
 * Marker contract for enums that have a stable database code.
 */
public interface EnumCode {

    /**
     * @return string code stored in the dictionary table.
     */
    String getCode();
}
