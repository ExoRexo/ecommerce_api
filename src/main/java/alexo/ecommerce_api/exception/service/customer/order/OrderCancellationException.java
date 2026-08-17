package alexo.ecommerce_api.exception.service.customer.order;

public class OrderCancellationException extends RuntimeException {
    public OrderCancellationException(String message) {
        super(message);
    }

    public static OrderCancellationException orderIsNotInCreatedOrPendingPaymentStatus(Long orderId) {
        return new OrderCancellationException("order with id["+orderId+"] is not in CREATED or PENDING_PAYMENT status");
    }
}
