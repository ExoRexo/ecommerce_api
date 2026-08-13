package alexo.ecommerce_api.service.internal.customer.order;

import alexo.ecommerce_api.cache.customer.order.OrderCacheService;
import alexo.ecommerce_api.dto.service.internal.customer.order.creation.OrderCreationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationResponseDTO;
import alexo.ecommerce_api.entity.customer.cart.CartItem;
import alexo.ecommerce_api.entity.customer.order.CustomerOrder;
import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import alexo.ecommerce_api.entity.customer.order.OrderItem;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.exception.service.customer.order.reservation.OrderCreationException;
import alexo.ecommerce_api.repository.customer.CartItemRepository;
import alexo.ecommerce_api.repository.customer.CustomerOrderRepository;
import alexo.ecommerce_api.repository.customer.CustomerRepository;
import alexo.ecommerce_api.repository.customer.OrderItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@AllArgsConstructor
public class OrderService {
    private final OrderItemReservationService orderItemReservationService;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final OrderCacheService orderCacheService;
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderCreationResponseDTO createOrder(Long customerId) {
        Assert.notNull(customerId, "customerId must be not null");

        List<CartItem> cartItems = cartItemRepository.findAllByCart_CustomerIdForUpdate(customerId);

        if (cartItems.isEmpty()) {
            throw OrderCreationException.cartIsEmpty(customerId);
        }

        CustomerOrder order = customerOrderRepository.save(
                CustomerOrder.builder()
                        .customer(customerRepository.getReferenceById(customerId))
                        .statusType(
                                Optional.ofNullable(orderCacheService.getCustomerOrderStatusTypes().get(CustomerOrderStatusType.CustomerOrderStatusCode.CREATED))
                                        .orElseThrow((() -> new EntityNotFoundException("customer status type with code [CREATED] is not found")))
                        )
                        .build()
        );

        List<OrderItemReservationResponseDTO> reservationResponseDTOS = new ArrayList<>(cartItems.size());

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = orderItemRepository.save(
                    OrderItem.builder()
                            .quantity(cartItem.getQuantity())
                            .product(cartItem.getProduct())
                            .unitPriceRub(cartItem.getProduct().getPriceRub())
                            .priceTotalRub(cartItem.getProduct().getPriceRub().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                            .order(order)
                            .build()
            );

            reservationResponseDTOS.add(orderItemReservationService.createOrderItemReservation(new OrderItemReservationRequestDTO(
                    orderItem.getId(),
                    orderItem.getQuantity(),
                    // for example im using warehouse id 1 of long int,
                    // in the best way it must be some kind of algo,
                    // that finds warehouses that satisfies product demand,
                    // and maybe it can be related to warehouse physical location and user physical location,
                    // but for learning project it's just too much
                    Warehouse.WAREHOUSE_ID_THAT_JUST_EXISTS
            )));

        }

        cartItemRepository.deleteAll(cartItems);

        return new OrderCreationResponseDTO(
                new OrderCreationResponseDTO.CustomerOrderDTO(
                        order.getId(),
                        new OrderCreationResponseDTO.CustomerOrderDTO.StatusTypeDTO(
                                order.getStatusType().getLabel(),
                                order.getStatusType().getDescription(),
                                order.getStatusType().getCode()
                        )
                ),
                reservationResponseDTOS
        );
    }

}
