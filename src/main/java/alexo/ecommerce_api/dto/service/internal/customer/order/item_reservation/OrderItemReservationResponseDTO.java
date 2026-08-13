package alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation;

import alexo.ecommerce_api.entity.customer.order.OrderItemReservationStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;


public record OrderItemReservationResponseDTO(
        Long reservationId,
        Integer reservedQuantity,
        OffsetDateTime createdAt,
        StatusTypeDTO statusType,
        WarehouseDTO warehouse
) {

    @Schema(name = "OrderItemReservationResponseDTO.WarehouseDTO")
    public record WarehouseDTO(
            Long id,
            String name,
            WarehouseAddressDTO address
    ){

        @Schema(name = "OrderItemReservationResponseDTO.WarehouseDTO.WarehouseAddressDTO")
        public record WarehouseAddressDTO(
                String address,
                String mailIndex,
                String country,
                String city
        ) {
        }
    }

    @Schema(name = "OrderItemReservationResponseDTO.StatusTypeDTO")
    public record StatusTypeDTO(
            String label,
            String description,
            OrderItemReservationStatusType.OrderItemReservationStatusCode code
    ){
    }

}
