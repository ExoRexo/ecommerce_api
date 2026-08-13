package alexo.ecommerce_api.dto.service.internal.customer.cart.update_product_qty;

public record UpdateProductQuantityInCartResponseDTO(
        Long productId,
        Integer quantity
) {}
