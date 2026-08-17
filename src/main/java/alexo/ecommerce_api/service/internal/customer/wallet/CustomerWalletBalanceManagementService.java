package alexo.ecommerce_api.service.internal.customer.wallet;

import alexo.ecommerce_api.cache.customer.wallet.CustomerWalletCacheService;
import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.CustomerWalletUpdateBalanceRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance.CustomerWalletUpdateBalanceResponseDTO;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Objects;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CustomerWalletUpdateBalanceResponseDTO updateCustomerWalletBalance(@Valid CustomerWalletUpdateBalanceRequestDTO requestDTO) {
        Assert.notNull(requestDTO, "request must be not null");

        BigDecimal delta = requestDTO.delta();
        Long customerId = requestDTO.customerId();

        CustomerWallet customerWallet = customerWalletRepository.findByCustomer_UserId_ForUpdate(customerId)
                .orElseThrow((() -> new EntityNotFoundException("customer wallet for customerId[" + customerId + "] is not found")));

        BigDecimal newBalance = customerWallet.getBalance().add(delta);

        if (newBalance.compareTo(MathUtil.BIG_DECIMAL_ZERO_SCALE_2) < 0) {
            throw CustomerWalletBalanceUpdateException.customerWalletBalanceBecomeLessThanZeroAfterBalanceUpdate(delta, newBalance, customerId);
        }

        CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode purposeCode = delta.compareTo(MathUtil.BIG_DECIMAL_ZERO_SCALE_2) < 0
                ? CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode.WITHDRAWAL
                : CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode.TOP_UP;

        CustomerWalletTransaction customerWalletTransaction = customerWalletTransactionRepository.save(
                CustomerWalletTransaction.builder()
                        .wallet(customerWallet)
                        .oldBalance(customerWallet.getBalance())
                        .newBalance(newBalance)
                        .delta(delta)
                        .purposeType(
                                Optional
                                        .ofNullable(
                                                customerWalletCacheService.getCustomerWalletTransactionPurposeTypes().get(purposeCode)
                                        )
                                        .orElseThrow((() -> new EntityNotFoundException("customer wallet transaction purpose for code[" + purposeCode + "] is not found")))
                        )
                        .user(userRepository.getReferenceById(Objects.requireNonNull(authorizationService.getCurrentUserPrincipalFromAuthentication()).getId()))
                        .build()
        );

        customerWallet.setBalance(newBalance);
        Long customerWalletTransactionId = customerWalletTransaction.getId();

        customerWalletTransaction = customerWalletTransactionRepository.findById_ForWalletUpdateResponse(customerWalletTransactionId)
                .orElseThrow((() -> new EntityNotFoundException("customer wallet transaction with id[" + customerWalletTransactionId + "] is not found")));

        return new CustomerWalletUpdateBalanceResponseDTO(
                customerWalletTransaction.getId(),
                customerWalletTransaction.getOldBalance(),
                customerWalletTransaction.getNewBalance(),
                customerWalletTransaction.getDelta(),
                new CustomerWalletUpdateBalanceResponseDTO.UserDTO(
                        customerWalletTransaction.getUser().getId(),
                        customerWalletTransaction.getUser().getFirstName(),
                        customerWalletTransaction.getUser().getLastName()
                ),
                new CustomerWalletUpdateBalanceResponseDTO.PurposeTypeDTO(
                        customerWalletTransaction.getPurposeType().getLabel(),
                        customerWalletTransaction.getPurposeType().getDescription(),
                        customerWalletTransaction.getPurposeType().getCode()
                ),
                customerWalletTransaction.getCreatedAt()
        );
    }

}
