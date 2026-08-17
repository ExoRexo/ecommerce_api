package alexo.ecommerce_api.http.controller.customer.order;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.cancellation.OrderCancellationRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.cancellation.OrderCancellationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.completion.OrderCompletionRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.completion.OrderCompletionResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.creation.OrderCreationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.details.OrderDetailsRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.details.OrderDetailsResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.list.OrderListRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.list.OrderListResponseDTO;
import alexo.ecommerce_api.entity.customer.order.CustomerOrderStatusType;
import alexo.ecommerce_api.service.internal.customer.order.OrderService;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/api/customer/order")
public class OrderController {
    private final OrderService orderService;
    private final AuthorizationService authorizationService;

    @PostMapping("/me")
    public ResponseEntity<@NotNull ApiResponseDTO<OrderCreationResponseDTO>> createOrder() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseDTO.success(
                                orderService.createOrder(authorizationService.getCurrentUserIdFromAuthentication())
                        )
                );
    }

    @GetMapping("/me/order-details")
    public ResponseEntity<@NotNull ApiResponseDTO<OrderDetailsResponseDTO>> getMyOrderDetails(@Valid @RequestBody OrderDetailsRequestDTO requestDTO) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(
                            orderService.getOrderDetails(
                                    requestDTO.orderId(),
                                    authorizationService.getCurrentUserIdFromAuthentication()
                            )
                        )
                );
    }

    @GetMapping("/me/order-list")
    public ResponseEntity<@NotNull ApiResponseDTO<PageResponseDTO<OrderListResponseDTO>>> getMyOrderList(
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size,
            @RequestParam(name = "orderId", required = false) Long orderId,
            @RequestParam(name = "statusCode", required = false) CustomerOrderStatusType.CustomerOrderStatusCode statusCode,
            @RequestParam(name = "startDate", required = false) OffsetDateTime startDate,
            @RequestParam(name = "endDate", required = false) OffsetDateTime endDate
    ) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(orderService.getOrderList(
                        new OrderListRequestDTO(
                                new OrderListRequestDTO.SortDTO(
                                        sortField,
                                        sortDirection
                                ),
                                new OrderListRequestDTO.PaginationDTO(
                                        page,
                                        size
                                ),
                                new OrderListRequestDTO.FiltersDTO(
                                        orderId,
                                        statusCode,
                                        authorizationService.getCurrentUserIdFromAuthentication(),
                                        startDate,
                                        endDate
                                )
                        )
                )));
    }

    @PostMapping("/me/cancel")
    public ResponseEntity<@NotNull ApiResponseDTO<OrderCancellationResponseDTO>> cancelMyOrder(@Valid @RequestBody OrderCancellationRequestDTO requestDTO) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(orderService.cancelOrder(
                        requestDTO.orderId(),
                        authorizationService.getCurrentUserIdFromAuthentication()
                )));
    }

    @PostMapping("/cancel")
    //    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_CUSTOMER_ORDER_CANCEL')") // todo
    public ResponseEntity<@NotNull ApiResponseDTO<OrderCancellationResponseDTO>> cancelOrder(@Valid @RequestBody OrderCancellationRequestDTO requestDTO) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(orderService.cancelOrder(
                        requestDTO.orderId(),
                        null
                )));
    }

    @PostMapping("/complete")
    //    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_CUSTOMER_ORDER_COMPLETE')") // todo
    public ResponseEntity<@NotNull ApiResponseDTO<OrderCompletionResponseDTO>> completeOrder(@Valid @RequestBody OrderCompletionRequestDTO requestDTO) {
        // any checks of token from other API or microservice, for example...
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(orderService.completeOrder(requestDTO.orderId())));
    }
}
