package alexo.ecommerce_api.mapper.customer.order.list;

import alexo.ecommerce_api.dto.service.internal.customer.order.list.OrderListResponseDTO;
import alexo.ecommerce_api.entity.customer.order.CustomerOrder;

public class OrderListMapper {
    public static OrderListResponseDTO fromCustomerOrderToOrderListResponseDTO(CustomerOrder order) {
        return new OrderListResponseDTO(
                order.getId(),
                order.getCreatedAt(),
                new OrderListResponseDTO.StatusTypeDTO(
                        order.getStatusType().getLabel(),
                        order.getStatusType().getDescription(),
                        order.getStatusType().getCode()
                )
        );
    }
}
