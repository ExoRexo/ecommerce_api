package alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;


public record ProductStockUpdateResponseDTO(
        Long transactionId,
        Integer oldQuantity,
        Integer newQuantity,
        Integer delta,
        WarehouseDTO warehouse,
        PurposeTypeDTO purpose,
        UserDTO user,
        ProductDTO product,
        OffsetDateTime createdAt
) {

    @Schema(name = "ProductStockUpdateResponseDTO.WarehouseDTO")
    public record WarehouseDTO(
            Long id,
            String name,
            WarehouseAddressDTO address
    ){
        @Schema(name = "ProductStockUpdateResponseDTO.WarehouseDTO.WarehouseAddressDTO")
        public record WarehouseAddressDTO(
                String address,
                String mailIndex,
                String country,
                String city
        ) {
        }
    }

    @Schema(name = "ProductStockUpdateResponseDTO.PurposeTypeDTO")
    public record PurposeTypeDTO(
            String label,
            String description,
            WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode code
    ){
    }

    @Schema(name = "ProductStockUpdateResponseDTO.UserDTO")
    public record UserDTO(
            Long id,
            String firstName,
            String lastName
    ){
    }

    @Schema(name = "ProductStockUpdateResponseDTO.ProductDTO")
    public record ProductDTO(
            Long id,
            String name
    ){
    }

}
