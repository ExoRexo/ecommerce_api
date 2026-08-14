package alexo.ecommerce_api.entity.customer.order;

import alexo.ecommerce_api.contract.enums.EnumCode;
import alexo.ecommerce_api.entity.converter.CustomerOrderStatusCodeConverter;
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

    @Convert(converter = CustomerOrderStatusCodeConverter.class)
    @Column(nullable = false, unique = true, length = 50)
    private CustomerOrderStatusCode code;

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
        CustomerOrderStatusType that = (CustomerOrderStatusType) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    /**
     * Business order statuses stored in customer_order_status_types.code.
     */
    @Getter
    @RequiredArgsConstructor
    public enum CustomerOrderStatusCode implements EnumCode {
        CREATED(false),
        PENDING_PAYMENT(false),
        PAID(false),
        COMPLETED(true),
        CANCELLED(true);

        private final boolean isFinal;
    }
}
