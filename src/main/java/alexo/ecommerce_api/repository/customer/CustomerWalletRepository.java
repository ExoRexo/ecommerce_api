package alexo.ecommerce_api.repository.customer;

import alexo.ecommerce_api.entity.customer.wallet.CustomerWallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerWalletRepository extends JpaRepository<CustomerWallet, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
select cw from CustomerWallet cw where cw.customer.userId = :customerUserId
""")
    Optional<CustomerWallet> findByCustomer_UserId_ForUpdate(Long customerUserId);
}

