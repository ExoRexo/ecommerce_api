package alexo.ecommerce_api.mapper.customer.order.item_reservation;

import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationResponseDTO;
import alexo.ecommerce_api.entity.customer.order.OrderItemWarehouseReservation;

public class OrderItemWarehouseReservationMapper {
    public static OrderItemReservationResponseDTO fromOrderItemWarehouseReservationToOrderItemReservationResponseDTO(OrderItemWarehouseReservation orderItemWarehouseReservation) {
        return new OrderItemReservationResponseDTO(
                orderItemWarehouseReservation.getId(),
                orderItemWarehouseReservation.getReservedQuantity(),
                orderItemWarehouseReservation.getCreatedAt(),
                new OrderItemReservationResponseDTO.StatusTypeDTO(
                        orderItemWarehouseReservation.getStatusType().getLabel(),
                        orderItemWarehouseReservation.getStatusType().getDescription(),
                        orderItemWarehouseReservation.getStatusType().getCode()
                ),
                new OrderItemReservationResponseDTO.WarehouseDTO(
                        orderItemWarehouseReservation.getWarehouse().getId(),
                        orderItemWarehouseReservation.getWarehouse().getName(),
                        new OrderItemReservationResponseDTO.WarehouseDTO.WarehouseAddressDTO(
                                orderItemWarehouseReservation.getWarehouse().getAddress().getAddress(),
                                orderItemWarehouseReservation.getWarehouse().getAddress().getMailIndex(),
                                orderItemWarehouseReservation.getWarehouse().getAddress().getCountry(),
                                orderItemWarehouseReservation.getWarehouse().getAddress().getCity()
                        )
                )
        );
    }
}
