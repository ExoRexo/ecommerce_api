package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.catalog.ProductStatusType;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link ProductStatusType.ProductStatusCode}.
 */
@Converter
public class ProductStatusCodeConverter extends AbstractEnumCodeAttributeConverter<ProductStatusType.ProductStatusCode> {

    public ProductStatusCodeConverter() {
        super(ProductStatusType.ProductStatusCode.class);
    }
}
