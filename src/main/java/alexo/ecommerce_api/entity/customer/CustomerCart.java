package alexo.ecommerce_api.entity.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Shopping cart attached to a customer.
 */
@Entity
@Table(name = "customer_carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCart {

    @Id
    private Long customerId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof CustomerCart other)) {
            return false;
        }

        return customerId != null && customerId.equals(other.customerId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
