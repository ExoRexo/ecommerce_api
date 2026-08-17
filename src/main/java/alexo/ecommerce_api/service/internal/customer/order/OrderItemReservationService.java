package alexo.ecommerce_api.service.internal.customer.order;

import alexo.ecommerce_api.cache.customer.order.OrderCacheService;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.item_reservation.OrderItemReservationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update.ProductStockUpdateRequestDTO;
import alexo.ecommerce_api.entity.customer.order.OrderItem;
import alexo.ecommerce_api.entity.customer.order.OrderItemReservationStatusType;
import alexo.ecommerce_api.entity.customer.order.OrderItemWarehouseReservation;
import alexo.ecommerce_api.entity.inventory.ProductWarehouseStock;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import alexo.ecommerce_api.exception.service.customer.order.reservation.OrderItemReservationCancellationException;
import alexo.ecommerce_api.exception.service.customer.order.reservation.OrderItemReservationException;
import alexo.ecommerce_api.exception.service.customer.order.reservation.OrderItemReservationFinishingException;
import alexo.ecommerce_api.mapper.customer.order.item_reservation.OrderItemWarehouseReservationMapper;
import alexo.ecommerce_api.repository.customer.order_item.OrderItemRepository;
import alexo.ecommerce_api.repository.customer.OrderItemWarehouseReservationRepository;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.service.internal.inventory.product_stock_management.ProductStockManagementService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@AllArgsConstructor
public class OrderItemReservationService {
    private final OrderCacheService orderCacheService;
    private final OrderItemWarehouseReservationRepository orderItemWarehouseReservationRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductWarehouseStockRepository productWarehouseStockRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductStockManagementService productStockManagementService;

    @Transactional
    public OrderItemReservationResponseDTO createOrderItemReservation(@Valid OrderItemReservationRequestDTO request) {
        Assert.notNull(request, "request must be not null");
        Long orderItemId = request.orderItemId();
        Long warehouseId = request.warehouseId();
        Integer quantityToReserve = request.quantityToReserve();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("orderItem with id[" + orderItemId + "] is not found"));
        Long productId = orderItem.getProduct().getId();

        if (orderItemWarehouseReservationRepository.existsByOrderItem_IdAndWarehouse_IdAndStatusType_Code(
                orderItemId,
                warehouseId,
                OrderItemReservationStatusType.OrderItemReservationStatusCode.ACTIVE
        )) {
            throw OrderItemReservationException.reservationForOrderItemAndWarehouseAlreadyExists(
                    orderItemId,
                    warehouseId
            );
        }

        Optional<ProductWarehouseStock> productWarehouseStockOption = productWarehouseStockRepository.findByWarehouse_IdAndProduct_IdForUpdate(warehouseId, productId);

        if (productWarehouseStockOption.isEmpty()) {
            throw OrderItemReservationException.warehouseProductStockForProductAndWarehouseNotFound(warehouseId, productId);
        }

        ProductWarehouseStock productWarehouseStock = productWarehouseStockOption.get();

        int physicalQuantity = productWarehouseStock.getPhysicalQuantity();
        int reservedQuantity = productWarehouseStock.getReservedQuantity();

        if (
                physicalQuantity == 0
                        ||
                        quantityToReserve > (physicalQuantity - reservedQuantity)
        ) {
            throw OrderItemReservationException.notEnoughStockForProductAndWarehouse(
                    warehouseId,
                    productId,
                    quantityToReserve
            );
        }

        productWarehouseStock.setReservedQuantity(reservedQuantity + quantityToReserve);

        OrderItemWarehouseReservation orderItemWarehouseReservation = orderItemWarehouseReservationRepository.save(
                OrderItemWarehouseReservation.builder()
                        .orderItem(orderItem)
                        .reservedQuantity(quantityToReserve)
                        .warehouse(warehouseRepository.getReferenceById(warehouseId))
                        .statusType(
                                Optional.ofNullable(orderCacheService.getOrderItemReservationStatusTypes().get(OrderItemReservationStatusType.OrderItemReservationStatusCode.ACTIVE))
                                        .orElseThrow(() -> new EntityNotFoundException("reservation status type with code[ACTIVE] is not found"))
                        )
                        .build()
        );

        Long orderItemWarehouseReservationId = orderItemWarehouseReservation.getId();

        orderItemWarehouseReservation = orderItemWarehouseReservationRepository
                .findByIdForItemReservationResponse(orderItemWarehouseReservationId)
                .orElseThrow(() -> new EntityNotFoundException("warehouse reservation with id["+orderItemWarehouseReservationId+"] is not found"));

        return OrderItemWarehouseReservationMapper.fromOrderItemWarehouseReservationToOrderItemReservationResponseDTO(orderItemWarehouseReservation);
    }

    @Transactional
    public OrderItemReservationResponseDTO cancelOrderItemReservation(Long orderItemWarehouseReservationId) {
        Optional<OrderItemWarehouseReservation> orderItemWarehouseReservationOption = orderItemWarehouseReservationRepository.findByIdForCancelForUpdate(orderItemWarehouseReservationId);

        if (
                orderItemWarehouseReservationOption.isEmpty()
                        || orderItemWarehouseReservationOption
                        .get()
                        .getStatusType()
                        .getCode() != OrderItemReservationStatusType.OrderItemReservationStatusCode.ACTIVE
        ) {
            throw OrderItemReservationCancellationException.activeReservationIsNotFound(orderItemWarehouseReservationId);
        }

        OrderItemWarehouseReservation orderItemWarehouseReservation = orderItemWarehouseReservationOption.get();
        Long warehouseId, productId;

        warehouseId = orderItemWarehouseReservation.getWarehouse().getId();
        productId = orderItemWarehouseReservation.getOrderItem().getProduct().getId();

        Optional<ProductWarehouseStock> productWarehouseStockOption = productWarehouseStockRepository.findByWarehouse_IdAndProduct_IdForUpdate(warehouseId, productId);

        if (productWarehouseStockOption.isEmpty()) {
            throw OrderItemReservationCancellationException.warehouseProductStockForProductAndWarehouseNotFound(warehouseId, productId);
        }

        ProductWarehouseStock productWarehouseStock = productWarehouseStockOption.get();
        orderItemWarehouseReservation.setStatusType(
                Optional.ofNullable(orderCacheService.getOrderItemReservationStatusTypes().get(OrderItemReservationStatusType.OrderItemReservationStatusCode.CANCELLED))
                        .orElseThrow(() -> new EntityNotFoundException("reservation status type with code[CANCELLED] is not found"))
        );

        int productWarehouseStockReservedQuantity = productWarehouseStock.getReservedQuantity()
                - orderItemWarehouseReservation.getReservedQuantity();

        if (productWarehouseStockReservedQuantity < 0) {
            throw OrderItemReservationCancellationException.productWarehouseStockReservedQuantityBecomeLessThan0AfterCancellation(warehouseId, productId);
        }

        productWarehouseStock.setReservedQuantity(productWarehouseStockReservedQuantity);

        return OrderItemWarehouseReservationMapper.fromOrderItemWarehouseReservationToOrderItemReservationResponseDTO(orderItemWarehouseReservationRepository
                .findByIdForItemReservationResponse(orderItemWarehouseReservationId)
                .orElseThrow(() -> new EntityNotFoundException("warehouse reservation with id["+orderItemWarehouseReservationId+"] is not found")));
    }

    @Transactional
    public OrderItemReservationResponseDTO finishOrderItemReservation(Long orderItemWarehouseReservationId) {
        Optional<OrderItemWarehouseReservation> orderItemWarehouseReservationOption = orderItemWarehouseReservationRepository.findByIdForCancelForUpdate(orderItemWarehouseReservationId);

        if (
                orderItemWarehouseReservationOption.isEmpty()
                        || orderItemWarehouseReservationOption
                        .get()
                        .getStatusType()
                        .getCode() != OrderItemReservationStatusType.OrderItemReservationStatusCode.ACTIVE
        ) {
            throw OrderItemReservationFinishingException.activeReservationIsNotFound(orderItemWarehouseReservationId);
        }

        OrderItemWarehouseReservation orderItemWarehouseReservation = orderItemWarehouseReservationOption.get();
        Long warehouseId, productId;

        warehouseId = orderItemWarehouseReservation.getWarehouse().getId();
        productId = orderItemWarehouseReservation.getOrderItem().getProduct().getId();

        orderItemWarehouseReservation.setStatusType(
                Optional.ofNullable(orderCacheService.getOrderItemReservationStatusTypes().get(OrderItemReservationStatusType.OrderItemReservationStatusCode.FINISHED))
                        .orElseThrow(() -> new EntityNotFoundException("reservation status type with code[FINISHED] is not found"))
        );

        ProductWarehouseStock stock = productWarehouseStockRepository
                .findByProductIdAndWarehouseIdForUpdate(
                        productId,
                        warehouseId
                )
                .orElseThrow(() -> new EntityNotFoundException("stock item with productId[" + productId + "] and warehouseId["+warehouseId+"] is not found"));

        stock.setReservedQuantity(stock.getReservedQuantity() - orderItemWarehouseReservation.getReservedQuantity());

        productStockManagementService.updateProductPhysicalStockOnWarehouse(new ProductStockUpdateRequestDTO(
                productId,
                warehouseId,
                - orderItemWarehouseReservation.getReservedQuantity(),
                WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode.SALE
        ));

        return OrderItemWarehouseReservationMapper.fromOrderItemWarehouseReservationToOrderItemReservationResponseDTO(orderItemWarehouseReservationRepository
                .findByIdForItemReservationResponse(orderItemWarehouseReservationId)
                .orElseThrow(() -> new EntityNotFoundException("warehouse reservation with id["+orderItemWarehouseReservationId+"] is not found")));
    }
}
