package alexo.ecommerce_api.entity.converter;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode}.
 */
@Converter
public class WarehouseStockTransactionPurposeCodeConverter
    extends AbstractEnumCodeAttributeConverter<WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode> {

    public WarehouseStockTransactionPurposeCodeConverter() {
        super(WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode.class);
    }
}
