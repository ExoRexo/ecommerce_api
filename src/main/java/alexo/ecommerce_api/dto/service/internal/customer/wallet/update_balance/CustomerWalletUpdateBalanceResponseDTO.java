package alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance;

import alexo.ecommerce_api.entity.customer.wallet.CustomerWalletTransactionPurposeType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CustomerWalletUpdateBalanceResponseDTO(
        Long transactionId,
        BigDecimal oldBalance,
        BigDecimal newBalance,
        BigDecimal delta,
        UserDTO user,
        PurposeTypeDTO purpose,
        OffsetDateTime createdAt
) {

    @Schema(name = "CustomerWalletUpdateBalanceResponseDTO.UserDTO")
    public record UserDTO(
            Long id,
            String lastName,
            String firstName
    ){
    }

    @Schema(name = "CustomerWalletUpdateBalanceResponseDTO.PurposeTypeDTO")
    public record PurposeTypeDTO(
            String label,
            String description,
            CustomerWalletTransactionPurposeType.CustomerWalletTransactionPurposeCode code
    ){
    }
}
