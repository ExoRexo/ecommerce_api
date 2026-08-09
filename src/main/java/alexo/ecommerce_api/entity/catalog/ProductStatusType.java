package alexo.ecommerce_api.entity.catalog;

import alexo.ecommerce_api.entity.converter.ProductStatusCodeConverter;
import alexo.ecommerce_api.entity.enums.ProductStatusCode;
import jakarta.persistence.*;
import lombok.*;

/**
 * Dictionary row describing a product status.
 */
@Entity
@Table(name = "product_status_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Convert(converter = ProductStatusCodeConverter.class)
    @Column(nullable = false, unique = true, length = 100)
    private ProductStatusCode code;

    @Column(nullable = false, length = 120)
    private String label;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof ProductStatusType other)) {
            return false;
        }

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
