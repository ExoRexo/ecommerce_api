package alexo.ecommerce_api.exception.service.customer.wallet;

import java.math.BigDecimal;

public class CustomerWalletBalanceUpdateException extends RuntimeException {
    public CustomerWalletBalanceUpdateException(String message) {
        super(message);
    }

    public static CustomerWalletBalanceUpdateException customerWalletBalanceBecomeLessThanZeroAfterBalanceUpdate(BigDecimal delta, BigDecimal newBalance, Long customerId) {
        return new CustomerWalletBalanceUpdateException("wallet balance of customer customer with id["+customerId+"] become < 0, ["+newBalance+"] after changing the balance with delta["+delta+"]");
    }
}
