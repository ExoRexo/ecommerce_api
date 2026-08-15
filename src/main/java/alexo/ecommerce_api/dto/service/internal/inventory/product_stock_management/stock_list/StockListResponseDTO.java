package alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.stock_list;

import io.swagger.v3.oas.annotations.media.Schema;

public record StockListResponseDTO(
        Integer physicalQuantity,
        Integer reservedQuantity,
        Integer freeQuantity,
        WarehouseDTO warehouse,
        ProductDTO product
) {

    @Schema(name = "StockListResponseDTO.WarehouseDTO")
    public record WarehouseDTO(
            Long id,
            String name,
            WarehouseAddressDTO address
    ) {
        @Schema(name = "StockListResponseDTO.WarehouseDTO.WarehouseAddressDTO")
        public record WarehouseAddressDTO(
                String address,
                String mailIndex,
                String country,
                String city
        ) {
        }
    }
    
    @Schema(name = "StockListResponseDTO.ProductDTO")
    public record ProductDTO(
            Long id,
            String name
    ) {
    }
}


