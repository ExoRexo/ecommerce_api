package alexo.ecommerce_api.dto.service.internal.customer.order.details;

import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import alexo.ecommerce_api.entity.customer.order.OrderItemReservationStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderDetailsResponseDTO(
        Long orderId,
        OffsetDateTime createdAt,
        OrderStatusTypeDTO status,
        List<ItemDTO> items
) {

    @Schema(name = "OrderDetailsResponseDTO.OrderStatusTypeDTO")
    public record OrderStatusTypeDTO(
            String label,
            String description,
            CustomerOrderStatusType.CustomerOrderStatusCode code
    ) {
    }

    @Schema(name = "OrderDetailsResponseDTO.ItemDTO")
    public record ItemDTO(
            Long itemId,
            Integer quantity,
            ProductDTO product,
            BigDecimal unitPriceRub,
            BigDecimal priceTotalRub,
            List<OrderItemWarehouseReservationDTO> warehouseReservations,
            OffsetDateTime createdAt
    ) {

        @Schema(name = "OrderDetailsResponseDTO.ItemDTO.OrderItemWarehouseReservationDTO")
        public record OrderItemWarehouseReservationDTO(
                Integer reservedQuantity,
                Long warehouseId,
                ReservationStatusTypeDTO status
        ){

            @Schema(name = "OrderDetailsResponseDTO.ItemDTO.OrderItemWarehouseReservationDTO.ReservationStatusTypeDTO")
            public record ReservationStatusTypeDTO(
                    String label,
                    String description,
                    OrderItemReservationStatusType.OrderItemReservationStatusCode code
            ) {

            }

        }

        @Schema(name = "OrderDetailsResponseDTO.ItemDTO.ProductDTO")
        public record ProductDTO(
                Long id,
                String name
        ){
        }

    }
}