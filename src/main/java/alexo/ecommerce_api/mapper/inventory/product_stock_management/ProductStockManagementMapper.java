package alexo.ecommerce_api.mapper.inventory.product_stock_management;

import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.transaction_list.TransactionListResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.product_stock_management.update_product_wh_stock.update.ProductStockUpdateResponseDTO;
import alexo.ecommerce_api.entity.inventory.WarehouseStockTransaction;

public class ProductStockManagementMapper {

    public static ProductStockUpdateResponseDTO fromWarehouseStockTransactionToProductStockUpdateResponseDTO(WarehouseStockTransaction warehouseStockTransaction) {
        return new ProductStockUpdateResponseDTO(
                warehouseStockTransaction.getId(),
                warehouseStockTransaction.getOldQuantity(),
                warehouseStockTransaction.getNewQuantity(),
                warehouseStockTransaction.getDelta(),
                new ProductStockUpdateResponseDTO.WarehouseDTO(
                        warehouseStockTransaction.getWarehouse().getId(),
                        warehouseStockTransaction.getWarehouse().getName(),

                        new ProductStockUpdateResponseDTO.WarehouseDTO.WarehouseAddressDTO(
                                warehouseStockTransaction.getWarehouse().getAddress().getAddress(),
                                warehouseStockTransaction.getWarehouse().getAddress().getMailIndex(),
                                warehouseStockTransaction.getWarehouse().getAddress().getCountry(),
                                warehouseStockTransaction.getWarehouse().getAddress().getCity()
                        )
                ),

                new ProductStockUpdateResponseDTO.PurposeTypeDTO(
                        warehouseStockTransaction.getPurposeType().getLabel(),
                        warehouseStockTransaction.getPurposeType().getDescription(),
                        warehouseStockTransaction.getPurposeType().getCode()
                ),

                new ProductStockUpdateResponseDTO.UserDTO(
                        warehouseStockTransaction.getUser().getId(),
                        warehouseStockTransaction.getUser().getFirstName(),
                        warehouseStockTransaction.getUser().getLastName()
                ),

                new ProductStockUpdateResponseDTO.ProductDTO(
                        warehouseStockTransaction.getProduct().getId(),
                        warehouseStockTransaction.getProduct().getName()
                ),

                warehouseStockTransaction.getCreatedAt()
        );
    }

    public static TransactionListResponseDTO fromWarehouseStockTransactionToTransactionListResponseDTO(WarehouseStockTransaction warehouseStockTransaction) {
        return new TransactionListResponseDTO(
                warehouseStockTransaction.getId(),
                warehouseStockTransaction.getOldQuantity(),
                warehouseStockTransaction.getNewQuantity(),
                warehouseStockTransaction.getDelta(),
                new TransactionListResponseDTO.WarehouseDTO(
                        warehouseStockTransaction.getWarehouse().getId(),
                        warehouseStockTransaction.getWarehouse().getName(),

                        new TransactionListResponseDTO.WarehouseDTO.WarehouseAddressDTO(
                                warehouseStockTransaction.getWarehouse().getAddress().getAddress(),
                                warehouseStockTransaction.getWarehouse().getAddress().getMailIndex(),
                                warehouseStockTransaction.getWarehouse().getAddress().getCountry(),
                                warehouseStockTransaction.getWarehouse().getAddress().getCity()
                        )
                ),

                new TransactionListResponseDTO.PurposeTypeDTO(
                        warehouseStockTransaction.getPurposeType().getLabel(),
                        warehouseStockTransaction.getPurposeType().getDescription(),
                        warehouseStockTransaction.getPurposeType().getCode()
                ),

                new TransactionListResponseDTO.UserDTO(
                        warehouseStockTransaction.getUser().getId(),
                        warehouseStockTransaction.getUser().getFirstName(),
                        warehouseStockTransaction.getUser().getLastName()
                ),

                new TransactionListResponseDTO.ProductDTO(
                        warehouseStockTransaction.getProduct().getId(),
                        warehouseStockTransaction.getProduct().getName()
                ),

                warehouseStockTransaction.getCreatedAt()
        );
    }
}
