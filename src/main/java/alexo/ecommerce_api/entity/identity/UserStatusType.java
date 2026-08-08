package alexo.ecommerce_api.entity.identity;

import alexo.ecommerce_api.entity.converter.UserStatusCodeConverter;
import alexo.ecommerce_api.entity.enums.UserStatusCode;
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
 * Dictionary row describing account status.
 */
@Entity
@Table(name = "user_status_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Convert(converter = UserStatusCodeConverter.class)
    @Column(nullable = false, unique = true, length = 100)
    private UserStatusCode code;

    @Column(nullable = false, length = 120)
    private String label;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof UserStatusType other)) {
            return false;
        }

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
