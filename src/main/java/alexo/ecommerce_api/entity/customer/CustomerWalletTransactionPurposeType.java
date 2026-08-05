package alexo.ecommerce_api.entity.customer;

import alexo.ecommerce_api.entity.common.EnumCodeMapper;
import alexo.ecommerce_api.entity.enums.CustomerWalletTransactionPurposeCode;
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
 * Dictionary row for customer wallet transaction purpose.
 */
@Entity
@Table(name = "c_wallt_transaction_purpose_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerWalletTransactionPurposeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * @return code converted to business enum.
     */
    @Transient
    public CustomerWalletTransactionPurposeCode getPurposeCodeEnum() {
        return EnumCodeMapper.fromCode(CustomerWalletTransactionPurposeCode.class, code);
    }

    /**
     * Sets database code from enum value.
     */
    public void setPurposeCodeEnum(CustomerWalletTransactionPurposeCode codeEnum) {
        this.code = codeEnum == null ? null : codeEnum.getCode();
    }
}
