package alexo.ecommerce_api.entity.customer;

import alexo.ecommerce_api.entity.converter.CustomerOrderStatusCodeConverter;
import alexo.ecommerce_api.entity.enums.CustomerOrderStatusCode;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
