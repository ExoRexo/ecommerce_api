package alexo.ecommerce_api.service.internal.customer.wallet;

import alexo.ecommerce_api.cache.customer.wallet.CustomerWalletCacheService;
import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.CustomerWalletUpdateBalanceRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.CustomerWalletUpdateBalanceResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.WithdrawMaximumAccessibleFromCustomerWalletRequestDTO;
import alexo.ecommerce_api.entity.customer.wallet.CustomerWallet;
import alexo.ecommerce_api.entity.customer.wallet.CustomerWalletTransaction;
import alexo.ecommerce_api.entity.customer.wallet.CustomerWalletTransactionPurposeType;
import alexo.ecommerce_api.exception.service.customer.wallet.CustomerWalletBalanceUpdateException;
import alexo.ecommerce_api.repository.customer.CustomerWalletRepository;
import alexo.ecommerce_api.repository.customer.CustomerWalletTransactionRepository;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import alexo.ecommerce_api.util.MathUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@Service
@Validated
public class CustomerWalletBalanceManagementService {

    private final CustomerWalletRepository customerWalletRepository;
    private final CustomerWalletTransactionRepository customerWalletTransactionRepository;
    private final CustomerWalletCacheService customerWalletCacheService;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Transactional
    public CustomerWalletUpdateBalanceResponseDTO updateCustomerWalletBalance(
            @Valid CustomerWalletUpdateBalanceRequestDTO requestDTO
    ) {
        Assert.notNull(requestDTO, "request must be not null");

        CustomerWallet customerWallet = findWalletForUpdate(requestDTO.customerId());

        return updateCustomerWalletBalance(
                customerWallet,
                requestDTO.delta()
        );
    }

    @Transactional
    public CustomerWalletUpdateBalanceResponseDTO withdrawMaximumAccessibleFromCustomerWalletBalance(
            @Valid WithdrawMaximumAccessibleFromCustomerWalletRequestDTO requestDTO
    ) {
        Assert.notNull(requestDTO, "request must be not null");

        CustomerWallet customerWallet = findWalletForUpdate(requestDTO.customerId());

        BigDecimal actualDelta = calculateAccessibleDelta(
                customerWallet.getBalance(),
                requestDTO.delta()
        );

        return updateCustomerWalletBalance(
                customerWallet,
                actualDelta
        );
    }

    private CustomerWallet findWalletForUpdate(Long customerId) {
        return customerWalletRepository.findByCustomer_UserId_ForUpdate(customerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "customer wallet for customerId[" + customerId + "] is not found"
                        )
                );
    }

    private BigDecimal calculateAccessibleDelta(
            BigDecimal balance,
            BigDecimal requestedDelta
    ) {
        if (balance.add(requestedDelta)
                .compareTo(MathUtil.BIG_DECIMAL_ZERO_SCALE_2) >= 0) {
            return requestedDelta;
        }

        return balance.negate();
    }

    private CustomerWalletUpdateBalanceResponseDTO updateCustomerWalletBalance(
            CustomerWallet customerWallet,
            BigDecimal delta
    ) {
        BigDecimal oldBalance = customerWallet.getBalance();
        BigDecimal newBalance = oldBalance.add(delta);

        if (newBalance.compareTo(MathUtil.BIG_DECIMAL_ZERO_SCALE_2) < 0) {
            throw CustomerWalletBalanceUpdateException.customerWalletBalanceBecomeLessThanZeroAfterBalanceUpdate(
                    delta,
                    newBalance,
                    customerWallet.getCustomer().getUserId()
            );
        }

        CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode purposeCode =
                delta.compareTo(MathUtil.BIG_DECIMAL_ZERO_SCALE_2) < 0
                        ? CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode.WITHDRAWAL
                        : CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode.TOP_UP;

        CustomerWalletTransactionPurposeType purposeType =
                Optional.ofNullable(
                        customerWalletCacheService
                                .getCustomerWalletTransactionPurposeTypes()
                                .get(purposeCode)
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "customer wallet transaction purpose for code[" + purposeCode + "] is not found"
                        )
                );

        Long currentUserId = authorizationService.getCurrentUserIdFromAuthentication();

        CustomerWalletTransaction customerWalletTransaction =
                customerWalletTransactionRepository.save(
                        CustomerWalletTransaction.builder()
                                .wallet(customerWallet)
                                .oldBalance(oldBalance)
                                .newBalance(newBalance)
                                .delta(delta)
                                .purposeType(purposeType)
                                .user(userRepository.getReferenceById(currentUserId))
                                .build()
                );

        customerWallet.setBalance(newBalance);

        Long customerWalletTransactionId = customerWalletTransaction.getId();

        CustomerWalletTransaction transaction = customerWalletTransactionRepository
                .findById_ForWalletUpdateResponse(customerWalletTransactionId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "customer wallet transaction with id["
                                        + customerWalletTransactionId
                                        + "] is not found"
                        )
                );

        return new CustomerWalletUpdateBalanceResponseDTO(
                transaction.getId(),
                transaction.getOldBalance(),
                transaction.getNewBalance(),
                transaction.getDelta(),
                new CustomerWalletUpdateBalanceResponseDTO.UserDTO(
                        transaction.getUser().getId(),
                        transaction.getUser().getFirstName(),
                        transaction.getUser().getLastName()
                ),
                new CustomerWalletUpdateBalanceResponseDTO.PurposeTypeDTO(
                        transaction.getPurposeType().getLabel(),
                        transaction.getPurposeType().getDescription(),
                        transaction.getPurposeType().getCode()
                ),
                transaction.getCreatedAt()
        );
    }

}