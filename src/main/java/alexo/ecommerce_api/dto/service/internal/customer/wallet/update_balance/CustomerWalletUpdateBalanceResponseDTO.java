package alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance;

import alexo.ecommerce_api.entity.customer.wallet.CustomerWalletTransactionPurposeType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CustomerWalletUpdateBalanceResponseDTO(
        @Nullable Long transactionId,
        @NotNull BigDecimal oldBalance,
        @NotNull BigDecimal newBalance,
        @NotNull BigDecimal delta,
        @Nullable UserDTO user,
        @Nullable PurposeTypeDTO purpose,
        @Nullable OffsetDateTime createdAt
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
