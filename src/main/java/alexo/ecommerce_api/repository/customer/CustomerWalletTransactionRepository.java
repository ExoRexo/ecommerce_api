package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.wallet.CustomerWalletTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerWalletTransactionRepository extends JpaRepository<CustomerWalletTransaction, Long> {
    @EntityGraph(attributePaths = {
            "purposeType",
            "user"
    })
    @Query("""
select c from CustomerWalletTransaction c where c.id = :id
""")
    Optional<CustomerWalletTransaction> findById_ForWalletUpdateResponse(Long id);
}

