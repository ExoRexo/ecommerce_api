package alexo.ecommerce_api.http.controller.customer.order;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.dto.service.internal.customer.order.creation.OrderCreationResponseDTO;
import alexo.ecommerce_api.service.internal.customer.order.OrderService;
import alexo.ecommerce_api.service.internal.identity.authority.AuthorizationService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
