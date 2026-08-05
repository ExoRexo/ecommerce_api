package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.common.EnumCodeAttributeConverter;
import alexo.ecommerce_api.entity.enums.WarehouseStockTransactionPurposeCode;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link WarehouseStockTransactionPurposeCode}.
 */
@Converter
public class WarehouseStockTransactionPurposeCodeConverter
    extends EnumCodeAttributeConverter<WarehouseStockTransactionPurposeCode> {

    public WarehouseStockTransactionPurposeCodeConverter() {
        super(WarehouseStockTransactionPurposeCode.class);
    }
}
