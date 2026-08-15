package alexo.ecommerce_api.service.internal.customer.order;

import alexo.ecommerce_api.cache.customer.order.OrderCacheService;
import alexo.ecommerce_api.dto.service.internal.customer.order.cancellation.OrderCancellationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.completion.OrderCompletionResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.creation.OrderCreationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationResponseDTO;
import alexo.ecommerce_api.entity.customer.cart.CartItem;
import alexo.ecommerce_api.entity.customer.order.CustomerOrder;
import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import alexo.ecommerce_api.entity.customer.order.OrderItem;
import alexo.ecommerce_api.entity.customer.order.OrderItemWarehouseReservation;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.exception.service.customer.order.reservation.OrderCancellationException;
import alexo.ecommerce_api.exception.service.customer.order.reservation.OrderCompletionException;
import alexo.ecommerce_api.exception.service.customer.order.reservation.OrderCreationException;
import alexo.ecommerce_api.mapper.customer.order.cancellation.OrderCancellationMapper;
import alexo.ecommerce_api.mapper.customer.order.completion.OrderCompletionMapper;
import alexo.ecommerce_api.mapper.customer.order.creation.OrderCreationMapper;
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

        order.setStatusType(
                Optional.ofNullable(orderCacheService.getCustomerOrderStatusTypes().get(CustomerOrderStatusType.CustomerOrderStatusCode.PENDING_PAYMENT))
                        .orElseThrow((() -> new EntityNotFoundException("customer status type with code [PENDING_PAYMENT] is not found")))
        );

        return OrderCreationMapper.fromOrder(order, reservationResponseDTOS);
    }

    @Transactional
    public OrderCancellationResponseDTO cancelOrder(Long orderId) {
        CustomerOrder order = customerOrderRepository.findByIdForCancelForUpdate(orderId)
                .orElseThrow((() -> new EntityNotFoundException("order with id[" + orderId + "] is not found")));

        CustomerOrderStatusType.CustomerOrderStatusCode statusCode = order.getStatusType().getCode();

        if (
                statusCode != CustomerOrderStatusType.CustomerOrderStatusCode.CREATED
                        && statusCode != CustomerOrderStatusType.CustomerOrderStatusCode.PENDING_PAYMENT
        ) {
            throw OrderCancellationException.orderIsNotInCreatedOrPendingPaymentStatus(orderId);
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderIdForCancelForUpdate(order.getId());

        List<OrderItemReservationResponseDTO> orderItemReservationResponseDTOS = new ArrayList<>(orderItems.size());

        for (OrderItem item : orderItems) {

            for (OrderItemWarehouseReservation warehouseReservation : item.getWarehouseReservations()) {
                orderItemReservationResponseDTOS.add(
                        orderItemReservationService.cancelOrderItemReservation(warehouseReservation.getId())
                );
            }

        }

        order.setStatusType(
                Optional.ofNullable(orderCacheService.getCustomerOrderStatusTypes().get(CustomerOrderStatusType.CustomerOrderStatusCode.CANCELLED))
                        .orElseThrow((() -> new EntityNotFoundException("customer status type with code [CANCELLED] is not found")))
        );

        return OrderCancellationMapper.fromOrder(order, orderItemReservationResponseDTOS);
    }

    /**
     * this method can be called from some kind of internal service,
     * web-hook, event e.t.c, which signalizes to method, that this order must be completed
     *
     * @param orderId customer order primary key
     * @return completion response dto
     */
    @Transactional
    public OrderCompletionResponseDTO completeOrder(Long orderId) {
        CustomerOrder order = customerOrderRepository.findByIdForCompleteForUpdate(orderId)
                .orElseThrow((() -> new EntityNotFoundException("order with id[" + orderId + "] is not found")));

        if (order.getStatusType().getCode() != CustomerOrderStatusType.CustomerOrderStatusCode.PAID) {
            throw OrderCompletionException.orderIsNotInPaidStatus(orderId);
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderIdForCompleteForUpdate(order.getId());

        List<OrderItemReservationResponseDTO> orderItemReservationResponseDTOS = new ArrayList<>(orderItems.size());

        for (OrderItem item : orderItems) {

            for (OrderItemWarehouseReservation warehouseReservation : item.getWarehouseReservations()) {
                orderItemReservationResponseDTOS.add(
                        orderItemReservationService.finishOrderItemReservation(warehouseReservation.getId())
                );
            }

        }

        order.setStatusType(
                Optional.ofNullable(orderCacheService.getCustomerOrderStatusTypes().get(CustomerOrderStatusType.CustomerOrderStatusCode.COMPLETED))
                        .orElseThrow((() -> new EntityNotFoundException("customer status type with code [COMPLETED] is not found")))
        );

        return OrderCompletionMapper.fromOrder(order, orderItemReservationResponseDTOS);
    }

}
