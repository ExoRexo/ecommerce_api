package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.enums.entity.ProductStatusCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link ProductStatusCode}.
 */
@Converter
public class ProductStatusCodeConverter extends AbstractEnumCodeAttributeConverter<ProductStatusCode> {

    public ProductStatusCodeConverter() {
        super(ProductStatusCode.class);
    }
}
