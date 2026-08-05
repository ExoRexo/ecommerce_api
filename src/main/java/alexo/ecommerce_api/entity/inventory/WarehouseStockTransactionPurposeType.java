package alexo.ecommerce_api.entity.inventory;

import alexo.ecommerce_api.entity.common.EnumCodeMapper;
import alexo.ecommerce_api.entity.enums.WarehouseStockTransactionPurposeCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * @return code converted to business enum.
     */
    @Transient
    public WarehouseStockTransactionPurposeCode getPurposeCodeEnum() {
        return EnumCodeMapper.fromCode(WarehouseStockTransactionPurposeCode.class, code);
    }

    /**
     * Sets database code from enum value.
     */
    public void setPurposeCodeEnum(WarehouseStockTransactionPurposeCode codeEnum) {
        this.code = codeEnum == null ? null : codeEnum.getCode();
    }
}
