package alexo.ecommerce_api.unit.service.internal.customer.wallet;

import alexo.ecommerce_api.cache.customer.wallet.CustomerWalletCacheService;
import alexo.ecommerce_api.repository.customer.CustomerWalletRepository;
import alexo.ecommerce_api.repository.customer.CustomerWalletTransactionRepository;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.service.internal.customer.wallet.CustomerWalletBalanceManagementService;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CustomerWalletBalanceManagementServiceTest {
    @Mock private CustomerWalletRepository walletRepository;
    @Mock private CustomerWalletTransactionRepository transactionRepository;
    @Mock private CustomerWalletCacheService walletCacheService;
    @Mock private UserRepository userRepository;
    @Mock private AuthorizationService authorizationService;
    @InjectMocks private CustomerWalletBalanceManagementService service;

    @Test
    void shouldRejectNullBalanceUpdateAndWithdrawal() {
        assertThatThrownBy(() -> service.updateCustomerWalletBalance(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.withdrawMaximumAccessibleFromCustomerWalletBalance(null)).isInstanceOf(IllegalArgumentException.class);
    }
}