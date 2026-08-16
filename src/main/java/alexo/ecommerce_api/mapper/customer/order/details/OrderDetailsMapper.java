package alexo.ecommerce_api.mapper.customer.order.details;

import alexo.ecommerce_api.dto.service.internal.customer.order.details.OrderDetailsResponseDTO;
import alexo.ecommerce_api.entity.customer.order.CustomerOrder;

public class OrderDetailsMapper {
    public static OrderDetailsResponseDTO fromOrderToOrderDetailsResponseDTO(CustomerOrder order) {
        return new OrderDetailsResponseDTO(
                order.getId(),
                order.getCreatedAt(),
                new OrderDetailsResponseDTO.OrderStatusTypeDTO(
                        order.getStatusType().getLabel(),
                        order.getStatusType().getDescription(),
                        order.getStatusType().getCode()
                ),
                order.getItems().stream().map(orderItem -> new OrderDetailsResponseDTO.ItemDTO(
                        orderItem.getId(),
                        orderItem.getQuantity(),
                        new OrderDetailsResponseDTO.ItemDTO.ProductDTO(
                                orderItem.getProduct().getId(),
                                orderItem.getProduct().getName()
                        ),
                        orderItem.getUnitPriceRub(),
                        orderItem.getPriceTotalRub(),
                        orderItem.getWarehouseReservations().stream().map(warehouseReservation -> new OrderDetailsResponseDTO.ItemDTO.OrderItemWarehouseReservationDTO(
                                warehouseReservation.getReservedQuantity(),
                                warehouseReservation.getWarehouse().getId(),

                                new OrderDetailsResponseDTO.ItemDTO.OrderItemWarehouseReservationDTO.ReservationStatusTypeDTO(
                                        warehouseReservation.getStatusType().getLabel(),
                                        warehouseReservation.getStatusType().getDescription(),
                                        warehouseReservation.getStatusType().getCode()
                                )
                        )).toList(),
                        orderItem.getCreatedAt()
                )).toList()
        );
    }
}
