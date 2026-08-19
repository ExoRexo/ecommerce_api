package alexo.ecommerce_api.unit.service.internal.customer.wallet;

import alexo.ecommerce_api.cache.customer.wallet.CustomerWalletCacheService;
import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.WithdrawMaximumAccessibleFromCustomerWalletRequestDTO;
import alexo.ecommerce_api.entity.customer.Customer;
import alexo.ecommerce_api.entity.customer.wallet.CustomerWallet;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldNotCreateTransactionWhenAccessibleDeltaIsZero() {
        CustomerWallet wallet = CustomerWallet.builder()
                .customerId(1L)
                .customer(Customer.builder().userId(1L).build())
                .balance(new BigDecimal("0.00"))
                .build();

        when(walletRepository.findByCustomer_UserId_ForUpdate(1L)).thenReturn(Optional.of(wallet));

        var response = service.withdrawMaximumAccessibleFromCustomerWalletBalance(
                new WithdrawMaximumAccessibleFromCustomerWalletRequestDTO(1L, new BigDecimal("-10.00"))
        );

        assertThat(response.transactionId()).isNull();
        assertThat(response.delta()).isEqualByComparingTo("0.00");
        assertThat(response.oldBalance()).isEqualByComparingTo("0.00");
        assertThat(response.newBalance()).isEqualByComparingTo("0.00");
        verify(transactionRepository, never()).save(any());
    }
}