package alexo.ecommerce_api.entity.customer;

import alexo.ecommerce_api.entity.converter.OrderItemReservationStatusCodeConverter;
import alexo.ecommerce_api.entity.enums.OrderItemReservationStatusCode;
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

    @Convert(converter = OrderItemReservationStatusCodeConverter.class)
    @Column(nullable = false, unique = true, length = 50)
    private OrderItemReservationStatusCode code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
