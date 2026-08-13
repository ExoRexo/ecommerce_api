package alexo.ecommerce_api.exception.service.customer.order.reservation;

public class OrderCreationException extends RuntimeException {
    public OrderCreationException(String message) {
        super(message);
    }

    public static OrderCreationException cartIsEmpty(Long customerId) {
        return new OrderCreationException("cart of customerId["+customerId+"] is empty");
    }
}
