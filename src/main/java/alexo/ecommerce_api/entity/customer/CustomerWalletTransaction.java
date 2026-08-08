package alexo.ecommerce_api.entity.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Wallet balance mutation event.
 */
@Entity
@Table(name = "customer_wallet_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerWalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private CustomerWallet wallet;

    @Column(name = "old_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal oldBalance;

    @Column(name = "new_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal newBalance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal delta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purpose_type_id", nullable = false)
    private CustomerWalletTransactionPurposeType purposeType;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof CustomerWalletTransaction other)) {
            return false;
        }

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
