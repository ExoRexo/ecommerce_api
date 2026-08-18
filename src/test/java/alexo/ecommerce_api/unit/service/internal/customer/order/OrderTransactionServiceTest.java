package alexo.ecommerce_api.unit.service.internal.customer.order;

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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
}