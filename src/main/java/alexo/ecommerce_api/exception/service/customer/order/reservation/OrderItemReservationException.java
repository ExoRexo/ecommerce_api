package alexo.ecommerce_api.exception.service.customer.order.reservation;

public class OrderItemReservationException extends RuntimeException {
    public OrderItemReservationException(String message) {
        super(message);
    }

    public static OrderItemReservationException reservationForOrderItemAndWarehouseAlreadyExists(Long orderItemId, Long warehouseId) {
        return new OrderItemReservationException("active reservation for order item["+orderItemId+"] and warehouse["+warehouseId+"] is already exists, to reserve this item you must cancel current reservation for this item");
    }

    public static OrderItemReservationException warehouseProductStockForProductAndWarehouseNotFound(Long warehouseId, Long productId) {
        return new OrderItemReservationException("warehouse product stock for product["+productId+"] and warehouse["+warehouseId+"] is not found");
    }

    public static OrderItemReservationException notEnoughStockForProductAndWarehouse(Long warehouseId, Long productId, Integer requiredQuantity) {
        return new OrderItemReservationException("warehouse product stock for product["+productId+"] and warehouse["+warehouseId+"] cannot provide requiredQuantity["+requiredQuantity+"]");
    }
}
