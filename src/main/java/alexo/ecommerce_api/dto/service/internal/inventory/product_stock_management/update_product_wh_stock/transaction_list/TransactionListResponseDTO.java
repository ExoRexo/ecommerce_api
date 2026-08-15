package alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.transaction_list;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record  TransactionListResponseDTO(
        Long transactionId,
        Integer oldQuantity,
        Integer newQuantity,
        Integer delta,
        TransactionListResponseDTO.WarehouseDTO warehouse,
        TransactionListResponseDTO.PurposeTypeDTO purpose,
        TransactionListResponseDTO.UserDTO user,
        TransactionListResponseDTO.ProductDTO product,
        OffsetDateTime createdAt
) {

    @Schema(name = "TransactionListResponseDTO.WarehouseDTO")
    public record WarehouseDTO(
            Long id,
            String name,
            TransactionListResponseDTO.WarehouseDTO.WarehouseAddressDTO address
    ) {
        @Schema(name = "TransactionListResponseDTO.WarehouseDTO.WarehouseAddressDTO")
        public record WarehouseAddressDTO(
                String address,
                String mailIndex,
                String country,
                String city
        ) {
        }
    }

    @Schema(name = "TransactionListResponseDTO.PurposeTypeDTO")
    public record PurposeTypeDTO(
            String label,
            String description,
            WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode code
    ) {
    }

    @Schema(name = "TransactionListResponseDTO.UserDTO")
    public record UserDTO(
            Long id,
            String firstName,
            String lastName
    ) {
    }

    @Schema(name = "TransactionListResponseDTO.ProductDTO")
    public record ProductDTO(
            Long id,
            String name
    ) {
    }
}


