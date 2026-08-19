package alexo.ecommerce_api.unit.service.internal.customer.order;

import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.CustomerWalletUpdateBalanceResponseDTO;
import alexo.ecommerce_api.repository.customer.CustomerOrderRepository;
import alexo.ecommerce_api.repository.customer.CustomerWalletTransactionRepository;
import alexo.ecommerce_api.repository.customer.OrderCustomerWalletTransactionRepository;
import alexo.ecommerce_api.service.internal.customer.order.OrderTransactionService;
import alexo.ecommerce_api.service.internal.customer.wallet.CustomerWalletBalanceManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTransactionServiceTest {
    @Mock private CustomerWalletBalanceManagementService walletService;
    @Mock private CustomerOrderRepository orderRepository;
    @Mock private CustomerWalletTransactionRepository walletTransactionRepository;
    @Mock private OrderCustomerWalletTransactionRepository orderWalletTransactionRepository;
    @InjectMocks private OrderTransactionService service;

    @Test
    void shouldRejectNullOrderTransactionInputs() {
        assertThatThrownBy(() -> service.withdrawAccessibleAmountFromCustomerWalletOnOrderCreation(null, 1L, 2L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.returnAmountOnCustomerWallet(BigDecimal.ONE, null, 2L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateOrderWalletTransactionWhenWithdrawDeltaIsZero() {
        when(walletService.withdrawMaximumAccessibleFromCustomerWalletBalance(any()))
                .thenReturn(new CustomerWalletUpdateBalanceResponseDTO(
                        null,
                        new BigDecimal("0.00"),
                        new BigDecimal("0.00"),
                        new BigDecimal("0.00"),
                        null,
                        null,
                        null
                ));

        BigDecimal leftovers = service.withdrawAccessibleAmountFromCustomerWalletOnOrderCreation(new BigDecimal("10.00"), 1L, 2L);

        assertThat(leftovers).isEqualByComparingTo("10.00");
        verify(orderWalletTransactionRepository, never()).save(any());
    }

    @Test
    void shouldNotCreateOrderWalletTransactionWhenReturnDeltaIsZero() {
        CustomerWalletUpdateBalanceResponseDTO zeroDeltaResponse = new CustomerWalletUpdateBalanceResponseDTO(
                null,
                new BigDecimal("100.00"),
                new BigDecimal("100.00"),
                new BigDecimal("0.00"),
                null,
                null,
                null
        );

        when(walletService.updateCustomerWalletBalance(any())).thenReturn(zeroDeltaResponse);

        CustomerWalletUpdateBalanceResponseDTO response = service.returnAmountOnCustomerWallet(new BigDecimal("0.00"), 1L, 2L);

        assertThat(response).isEqualTo(zeroDeltaResponse);
        verify(orderWalletTransactionRepository, never()).save(any());
    }
}