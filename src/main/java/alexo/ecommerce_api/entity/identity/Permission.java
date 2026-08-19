package alexo.ecommerce_api.entity.identity;

import alexo.ecommerce_api.contract.enums.EnumCode;
import alexo.ecommerce_api.entity.converter.PermissionCodeConverter;
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
 * Security permission dictionary.
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = PermissionCodeConverter.class)
    @Column(nullable = false, unique = true, length = 255)
    private PermissionCode code;

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
        Permission that = (Permission) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    /**
     * Permission values stored in permissions.code.
     */
    @Getter
    @RequiredArgsConstructor
    public enum PermissionCode implements EnumCode {
        CATALOG_PRODUCT_CREATE,
        CATALOG_PRODUCT_READ_LIST,
        CATALOG_PRODUCT_READ_STATUS_TYPES,
        CATALOG_PRODUCT_UPDATE,
        CATALOG_PRODUCT_UPDATE_PRICE_RUB,

        CATALOG_CATEGORY_CREATE,
        CATALOG_CATEGORY_UPDATE,
        CATALOG_CATEGORY_READ_LIST,
        CATALOG_CATEGORY_READ_TREE,
        CATALOG_CATEGORY_READ_CONCRETE,

        CUSTOMER_ORDER_CANCEL,
        CUSTOMER_ORDER_COMPLETE,

        INVENTORY_PRODUCT_STOCK_MANAGEMENT_UPDATE_WAREHOUSE_STOCK,
        INVENTORY_PRODUCT_STOCK_MANAGEMENT_READ_PRODUCT_WH_STOCKS_LIST,
        INVENTORY_PRODUCT_STOCK_MANAGEMENT_READ_WH_TRANSACTIONS_LIST,

        INVENTORY_WAREHOUSE_CREATE,
        INVENTORY_WAREHOUSE_UPDATE,
        INVENTORY_WAREHOUSE_READ_LIST
    }
}
