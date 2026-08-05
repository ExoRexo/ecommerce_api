package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.common.EnumCodeAttributeConverter;
import alexo.ecommerce_api.entity.enums.ProductStatusCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link ProductStatusCode}.
 */
@Converter
public class ProductStatusCodeConverter extends EnumCodeAttributeConverter<ProductStatusCode> {

    public ProductStatusCodeConverter() {
        super(ProductStatusCode.class);
    }
}
