package alexo.ecommerce_api.exception.service.customer.order;

public class OrderCompletionException extends RuntimeException {
    public OrderCompletionException(String message) {
        super(message);
    }

    public static OrderCompletionException orderIsNotInPaidStatus(Long orderId) {
        return new OrderCompletionException("order with id["+orderId+"] is not in PAID status");
    }
}
