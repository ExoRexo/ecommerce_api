package alexo.ecommerce_api.entity.inventory;

import alexo.ecommerce_api.contract.enums.EnumCode;
import alexo.ecommerce_api.contract.enums.EnumDescription;
import alexo.ecommerce_api.contract.enums.EnumLabel;
import alexo.ecommerce_api.entity.converter.WarehouseStockTransactionPurposeCodeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * Dictionary row for warehouse stock transaction purpose.
 */
@Entity
@Table(name = "wh_st_transaction_purpose_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseStockTransactionPurposeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Convert(converter = WarehouseStockTransactionPurposeCodeConverter.class)
    @Column(nullable = false, unique = true, length = 100)
    private WarehouseStockTransactionPurposeCode code;

    @Column(nullable = false, length = 120)
    private String label;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        WarehouseStockTransactionPurposeType that = (WarehouseStockTransactionPurposeType) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    /**
     * Warehouse stock transaction intents stored in wh_st_transaction_purpose_types.code.
     */
    @Getter
    @AllArgsConstructor
    public enum WarehouseStockTransactionPurposeCode implements EnumCode {
        SALE(
                new WarehouseStockTransactionOperationCode[]{
                        WarehouseStockTransactionOperationCode.DECREASE
                }
        ),
        PURCHASE(
                new WarehouseStockTransactionOperationCode[]{
                        WarehouseStockTransactionOperationCode.INCREASE
                }
        ),
        INVENTORY_ADJUSTMENT(
                new WarehouseStockTransactionOperationCode[]{
                        WarehouseStockTransactionOperationCode.INCREASE,
                        WarehouseStockTransactionOperationCode.DECREASE
                }
        );

        private final WarehouseStockTransactionOperationCode[] allowedOperations;
    }

    @Getter
    @AllArgsConstructor
    public enum WarehouseStockTransactionOperationCode implements EnumCode, EnumDescription, EnumLabel {
        INCREASE("Приход", "Приход единиц товара на склад"),
        DECREASE("Расход", "Расход единиц товара на складе");

        private final String label;
        private final String description;
    }
}
