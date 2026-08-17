package alexo.ecommerce_api.exception.service.customer.order;

public class OrderCreationException extends RuntimeException {
    public OrderCreationException(String message) {
        super(message);
    }

    public static OrderCreationException cartIsEmpty(Long customerId) {
        return new OrderCreationException("cart of customerId[" + customerId + "] is empty");
    }

    public static OrderCreationException leftoversAfterWithdrawBecomeLessThan0(Long customerId) {
        return new OrderCreationException("order amount sum leftovers after withdraw from user wallet become less than 0, customerId[" + customerId + "]");
    }
}
