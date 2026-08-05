package alexo.ecommerce_api.entity.catalog;

import alexo.ecommerce_api.entity.converter.ProductStatusCodeConverter;
import alexo.ecommerce_api.entity.enums.ProductStatusCode;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
