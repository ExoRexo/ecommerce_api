package alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock;

import alexo.ecommerce_api.entity.inventory.WarehouseStockTransactionPurposeType;

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

    public record WarehouseDTO(
            Long id,
            String name,
            WarehouseAddressDTO address
    ){
        public record WarehouseAddressDTO(
                String address,
                String mailIndex,
                String country,
                String city
        ) {
        }
    }

    public record PurposeTypeDTO(
            String label,
            String description,
            WarehouseStockTransactionPurposeType.WarehouseStockTransactionPurposeCode code
    ){
    }

    public record UserDTO(
            Long id,
            String firstName,
            String lastName
    ){
    }

    public record ProductDTO(
            Long id,
            String name
    ){
    }

}
