package alexo.ecommerce_api.exception.service.customer.order.reservation;

public class OrderItemReservationCancellationException extends RuntimeException {
    public OrderItemReservationCancellationException(String message) {
        super(message);
    }

    public static OrderItemReservationCancellationException activeReservationIsNotFound(Long orderItemWarehouseReservationId) {
        return new OrderItemReservationCancellationException("active reservation for orderItemWarehouseReservationId["+orderItemWarehouseReservationId+"] is not found");
    }

    public static OrderItemReservationCancellationException warehouseProductStockForProductAndWarehouseNotFound(Long warehouseId, Long productId) {
        return new OrderItemReservationCancellationException("warehouse product stock for product["+productId+"] and warehouse["+warehouseId+"] is not found");
    }

    public static OrderItemReservationCancellationException productWarehouseStockReservedQuantityBecomeLessThan0AfterCancellation(Long warehouseId, Long productId) {
        return new OrderItemReservationCancellationException("product stock for product["+productId+"] and warehouse["+warehouseId+"] become < 0 after reservation cancellation");
    }
}
