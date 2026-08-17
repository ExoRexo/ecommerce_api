package alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance;

import alexo.ecommerce_api.validation.numeric.NotZero;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CustomerWalletUpdateBalanceRequestDTO(
        @NotNull
        Long customerId,

        @NotZero
        @NotNull
        BigDecimal delta
) {
}
