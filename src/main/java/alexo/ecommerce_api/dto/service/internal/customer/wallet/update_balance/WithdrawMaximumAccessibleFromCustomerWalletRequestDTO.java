package alexo.ecommerce_api.dto.service.internal.customer.wallet.update_balance;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawMaximumAccessibleFromCustomerWalletRequestDTO(
        @NotNull
        Long customerId,

        @NotNull
        @Max(-1)
        BigDecimal delta
) {
}
