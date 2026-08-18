package alexo.ecommerce_api.service.internal.inventory.warehouse;

import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.WarehouseCreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update.WarehouseUpdateRequestDTO;
import alexo.ecommerce_api.entity.inventory.Address;
import alexo.ecommerce_api.entity.inventory.ProductWarehouseStock;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.repository.catalog.ProductRepository;
import alexo.ecommerce_api.repository.inventory.AddressRepository;
import alexo.ecommerce_api.repository.inventory.ProductWarehouseStockRepository;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.repository.lock.AdvisoryLockRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class WarehouseModifyingService {

    private final WarehouseRepository warehouseRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final ProductWarehouseStockRepository productWarehouseStockRepository;
    private final AdvisoryLockRepository advisoryLockRepository;

    @Transactional
    public Warehouse persistWarehouse(WarehouseCreateRequestDTO requestDTO) {
        Objects.requireNonNull(requestDTO, "request dto cannot be null");

        advisoryLockRepository.acquireTransactionLock(AdvisoryLockRepository.LockCode.INVENTORY_PRODUCT_WAREHOUSE_STOCK_MATRIX_LOCK_KEY);

        if (warehouseRepository.existsByName(requestDTO.name().trim())) {
            throw new EntityExistsException("warehouse with name [" + requestDTO.name().trim() + "] is already exists");
        }

        if (addressRepository.existsByAddress(requestDTO.address().address().trim())) {
            throw new EntityExistsException("address  [" + requestDTO.address().address().trim() + "] is already exists");
        }

        Warehouse warehouse = warehouseRepository.save(
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

        List<Long> productIds = productRepository.findAllIds();

        List<ProductWarehouseStock> productWarehouseStocks = new ArrayList<>(productIds.size());

        OffsetDateTime now = OffsetDateTime.now();

        for (Long productId : productIds) {

            productWarehouseStocks.add(
                    ProductWarehouseStock.builder()
                            .product(productRepository.getReferenceById(productId))
                            .warehouse(warehouse)
                            .physicalQuantity(0)
                            .reservedQuantity(0)
                            .updatedAt(now)
                            .build()
            );

        }

        productWarehouseStockRepository.saveAll(productWarehouseStocks);

        return warehouse;
    }

    @Transactional
    public Warehouse updateWarehouse(WarehouseUpdateRequestDTO requestDTO) {
        Objects.requireNonNull(requestDTO, "request dto cannot be null");

        Warehouse warehouse = warehouseRepository.findByIdForUpdate(requestDTO.warehouseId()).orElseThrow(() -> new EntityNotFoundException("warehouse with id [" + requestDTO.warehouseId() + "] is not found"));

        String warehouseName = warehouse.getName();
        Address warehouseAddress = warehouse.getAddress();
        String addressAddress = warehouseAddress.getAddress();
        String addressMailIndex = warehouseAddress.getMailIndex();
        String addressCountry = warehouseAddress.getCountry();
        String addressCity = warehouseAddress.getCity();

        JsonNullable<WarehouseUpdateRequestDTO.AddressDTO> nullableAddressFromDto = requestDTO.address();

        // address fill
        if (nullableAddressFromDto.isPresent()) {
            WarehouseUpdateRequestDTO.AddressDTO AddressFromDto = nullableAddressFromDto.get();

            if(AddressFromDto.address().isPresent()) {
                addressAddress = AddressFromDto.address().get();
            }

            if(AddressFromDto.mailIndex().isPresent()) {
                addressMailIndex = AddressFromDto.mailIndex().get();
            }

            if(AddressFromDto.country().isPresent()) {
                addressCountry = AddressFromDto.country().get();
            }

            if(AddressFromDto.city().isPresent()) {
                addressCity = AddressFromDto.city().get();
            }

        }

        // warehouse fill
        if (requestDTO.name().isPresent()) {
            warehouseName = requestDTO.name().get();
        }

        if (warehouseRepository.existsByNameAndIdNot(warehouseName, warehouse.getId())) {
            throw new EntityExistsException("warehouse with name [" + warehouseName + "] is already exists");
        }

        if (addressRepository.existsByAddressAndIdNot(addressAddress, warehouseAddress.getId())) {
            throw new EntityExistsException("address  [" + addressAddress + "] is already exists");
        }

        warehouse.setName(warehouseName);
        warehouseAddress.setAddress(addressAddress);
        warehouseAddress.setMailIndex(addressMailIndex);
        warehouseAddress.setCountry(addressCountry);
        warehouseAddress.setCity(addressCity);

        return warehouse;
    }
}
