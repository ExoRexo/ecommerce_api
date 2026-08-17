package alexo.ecommerce_api.service.internal.customer.order;

import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.CustomerWalletUpdateBalanceRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.CustomerWalletUpdateBalanceResponseDTO;
import alexo.ecommerce_api.entity.customer.order.OrderCustomerWalletTransaction;
import alexo.ecommerce_api.exception.service.customer.order.OrderTransactionException;
import alexo.ecommerce_api.exception.service.customer.wallet.CustomerWalletBalanceUpdateException;
import alexo.ecommerce_api.repository.customer.CustomerOrderRepository;
import alexo.ecommerce_api.repository.customer.CustomerWalletTransactionRepository;
import alexo.ecommerce_api.repository.customer.OrderCustomerWalletTransactionRepository;
import alexo.ecommerce_api.service.internal.customer.wallet.CustomerWalletBalanceManagementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Validated
public class OrderTransactionService {

    private final CustomerWalletBalanceManagementService customerWalletBalanceManagementService;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerWalletTransactionRepository customerWalletTransactionRepository;
    private final OrderCustomerWalletTransactionRepository orderCustomerWalletTransactionRepository;

    @Transactional
    public BigDecimal withdrawAccessibleAmountFromCustomerWalletOnOrderCreation(BigDecimal amountToWithdraw, Long customerId, Long orderId) {
        Assert.notNull(amountToWithdraw, "amountToWithdraw must be not null");
        Assert.notNull(customerId, "customerId must be not null");

        CustomerWalletUpdateBalanceResponseDTO updateBalanceResponseDTO;

        try {
            updateBalanceResponseDTO = customerWalletBalanceManagementService.updateCustomerWalletBalance(new CustomerWalletUpdateBalanceRequestDTO(
                    customerId,
                    amountToWithdraw.negate()
            ));
            // if it is not enough money on user's balance
        } catch (CustomerWalletBalanceUpdateException _) {
            return amountToWithdraw;
        }

        if (updateBalanceResponseDTO == null
                || updateBalanceResponseDTO.delta() == null
                || updateBalanceResponseDTO.transactionId() == null
        ) {
            throw OrderTransactionException.updateBalanceResponseDTOIsNullDuringSubtractionFromUserWallet(customerId, orderId);
        }

        orderCustomerWalletTransactionRepository.save(
                OrderCustomerWalletTransaction.builder()
                        .order(customerOrderRepository.getReferenceById(orderId))
                        .customerWalletTransaction(customerWalletTransactionRepository.getReferenceById(updateBalanceResponseDTO.transactionId()))
                        .build()
        );

        return amountToWithdraw.abs().subtract(updateBalanceResponseDTO.delta().abs());
    }
}
