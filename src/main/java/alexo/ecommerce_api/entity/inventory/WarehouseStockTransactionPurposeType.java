package alexo.ecommerce_api.entity.inventory;

import alexo.ecommerce_api.entity.converter.WarehouseStockTransactionPurposeCodeConverter;
import alexo.ecommerce_api.entity.enums.WarehouseStockTransactionPurposeCode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof WarehouseStockTransactionPurposeType other)) {
            return false;
        }

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
