package alexo.ecommerce_api.service.internal.inventory.warehouse;

import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.request.WarehouseCreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.response.AddressDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.response.WarehouseCreateResponseDTO;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class WarehouseService {

    private final WarehouseTransactionalService warehouseTransactionalService;

    public WarehouseCreateResponseDTO createWarehouse(WarehouseCreateRequestDTO requestDTO) {

        Warehouse warehouse = warehouseTransactionalService.persistWarehouse(requestDTO);

        return new WarehouseCreateResponseDTO(
                warehouse.getId(),
                warehouse.getName(),
                new AddressDTO(
                        warehouse.getAddress().getAddress(),
                        warehouse.getAddress().getMailIndex(),
                        warehouse.getAddress().getCountry(),
                        warehouse.getAddress().getCity()
                )
        );
    }
}
