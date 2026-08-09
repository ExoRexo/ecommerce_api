package alexo.ecommerce_api.entity.catalog;

import jakarta.persistence.*;
import lombok.*;

/**
 * Product category with optional hierarchy through parent reference.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Category other)) {
            return false;
        }

        return id != null && id.equals(other.id);
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
