package alexo.ecommerce_api.entity.customer;

import alexo.ecommerce_api.entity.common.EnumCodeMapper;
import alexo.ecommerce_api.entity.enums.OrderItemReservationStatusCode;
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
 * Dictionary row describing reservation status.
 */
@Entity
@Table(name = "order_item_reservation_status_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemReservationStatusType {

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
    public OrderItemReservationStatusCode getStatusCodeEnum() {
        return EnumCodeMapper.fromCode(OrderItemReservationStatusCode.class, code);
    }

    /**
     * Sets database code from enum value.
     */
    public void setStatusCodeEnum(OrderItemReservationStatusCode codeEnum) {
        this.code = codeEnum == null ? null : codeEnum.getCode();
    }
}
