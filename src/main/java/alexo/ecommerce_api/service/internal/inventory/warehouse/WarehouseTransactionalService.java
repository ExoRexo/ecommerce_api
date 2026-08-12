package alexo.ecommerce_api.service.internal.inventory.warehouse;

import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.request.WarehouseCreateRequestDTO;
import alexo.ecommerce_api.entity.inventory.Address;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.repository.inventory.AddressRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WarehouseTransactionalService {

    private final WarehouseRepository warehouseRepository;
    private final AddressRepository addressRepository;

    @Transactional
    protected Warehouse persistWarehouse(WarehouseCreateRequestDTO requestDTO) {

        if (warehouseRepository.existsByName(requestDTO.name().trim())) {
            throw new EntityExistsException("warehouse with name [" + requestDTO.name().trim() + "] is already exists");
        }

        if (addressRepository.existsByAddress(requestDTO.address().address().trim())) {
            throw new EntityExistsException("address  [" + requestDTO.address().address().trim() + "] is already exists");
        }

        return warehouseRepository.save(
                Warehouse.builder()
                        .name(requestDTO.name())
                        .address(
                                addressRepository.save(
                                        Address.builder()
                                                .address(requestDTO.address().address())
                                                .mailIndex(requestDTO.address().mailIndex())
                                                .country(requestDTO.address().country())
                                                .city(requestDTO.address().city())
                                                .build()
                                )
                        )
                        .build()
        );
    }
}
