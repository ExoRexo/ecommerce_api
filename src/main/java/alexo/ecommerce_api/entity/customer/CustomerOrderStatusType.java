package alexo.ecommerce_api.entity.customer;

import alexo.ecommerce_api.entity.common.EnumCodeMapper;
import alexo.ecommerce_api.entity.enums.CustomerOrderStatusCode;
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
 * Dictionary row describing customer order status.
 */
@Entity
@Table(name = "customer_order_status_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrderStatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * @return code converted to business enum.
     */
    @Transient
    public CustomerOrderStatusCode getStatusCodeEnum() {
        return EnumCodeMapper.fromCode(CustomerOrderStatusCode.class, code);
    }

    /**
     * Sets database code from enum value.
     */
    public void setStatusCodeEnum(CustomerOrderStatusCode codeEnum) {
        this.code = codeEnum == null ? null : codeEnum.getCode();
    }
}
