package alexo.ecommerce_api.http.controller.customer.order;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.cancellation.OrderCancellationRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.cancellation.OrderCancellationResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.completion.OrderCompletionRequestDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.completion.OrderCompletionResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.creation.OrderCreationResponseDTO;
import alexo.ecommerce_api.service.internal.customer.order.OrderService;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/api/customer/order")
public class OrderController {
    private final OrderService orderService;
    private final AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<@NotNull ApiResponseDTO<OrderCreationResponseDTO>> createOrder() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    ApiResponseDTO.success(
                            orderService
                                    .createOrder(
                                            Objects
                                                    .requireNonNull(authorizationService.getCurrentUserPrincipalFromAuthentication())
                                                    .getId()
                                    )
                    )
                );
    }

    @PostMapping("/cancel")
    public ResponseEntity<@NotNull ApiResponseDTO<OrderCancellationResponseDTO>> cancelOrder(@Valid @RequestBody OrderCancellationRequestDTO requestDTO) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(orderService.cancelOrder(requestDTO.orderId())));
    }

    @PostMapping("/complete")
    public ResponseEntity<@NotNull ApiResponseDTO<OrderCompletionResponseDTO>> completeOrder(@Valid @RequestBody OrderCompletionRequestDTO requestDTO) {
        // any checks of token from other API or microservice, for example...
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(orderService.completeOrder(requestDTO.orderId())));
    }
}
