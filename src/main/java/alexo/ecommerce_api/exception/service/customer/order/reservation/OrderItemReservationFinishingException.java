package alexo.ecommerce_api.exception.service.customer.order.reservation;

public class OrderItemReservationFinishingException extends RuntimeException {
    public OrderItemReservationFinishingException(String message) {
        super(message);
    }

    public static OrderItemReservationFinishingException activeReservationIsNotFound(Long orderItemWarehouseReservationId) {
        return new OrderItemReservationFinishingException("active reservation for orderItemWarehouseReservationId["+orderItemWarehouseReservationId+"] is not found");
    }

    public static OrderItemReservationFinishingException warehouseProductStockForProductAndWarehouseNotFound(Long warehouseId, Long productId) {
        return new OrderItemReservationFinishingException("warehouse product stock for product["+productId+"] and warehouse["+warehouseId+"] is not found");
    }

    public static OrderItemReservationFinishingException productWarehouseStockReservedQuantityBecomeLessThan0AfterCancellation(Long warehouseId, Long productId) {
        return new OrderItemReservationFinishingException("product stock for product["+productId+"] and warehouse["+warehouseId+"] become < 0 after reservation cancellation");
    }
}
