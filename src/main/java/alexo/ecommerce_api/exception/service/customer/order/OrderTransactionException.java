package alexo.ecommerce_api.exception.service.customer.order;

public class OrderTransactionException extends RuntimeException {
    public OrderTransactionException(String message) {
        super(message);
    }

    public static OrderTransactionException updateBalanceResponseDTOIsNullDuringSubtractionFromUserWallet(Long customerId, Long orderId) {
        return new OrderTransactionException("updateBalanceResponseDTO is null during amount subtraction during order creation customerId["+customerId+"] orderId["+orderId+"]");
    }
}
