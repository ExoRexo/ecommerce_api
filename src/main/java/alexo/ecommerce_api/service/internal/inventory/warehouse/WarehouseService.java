package alexo.ecommerce_api.service.internal.inventory.warehouse;

import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.WarehouseCreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.WarehouseCreateResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.list.RequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.list.WarehouseListResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update.WarehouseUpdateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update.WarehouseUpdateResponseDTO;
import alexo.ecommerce_api.entity.inventory.Warehouse;
import alexo.ecommerce_api.repository.inventory.WarehouseRepository;
import alexo.ecommerce_api.specification.inventory.warehouse.WarehouseSpecifications;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class WarehouseService {

    private final WarehouseModifyingService warehouseModifyingService;
    private final WarehouseRepository warehouseRepository;

    public WarehouseCreateResponseDTO createWarehouse(@Valid WarehouseCreateRequestDTO requestDTO) {

        Warehouse warehouse = warehouseModifyingService.persistWarehouse(requestDTO);

        return new WarehouseCreateResponseDTO(
                warehouse.getId(),
                warehouse.getName(),
                new WarehouseCreateResponseDTO.AddressDTO(
                        warehouse.getAddress().getAddress(),
                        warehouse.getAddress().getMailIndex(),
                        warehouse.getAddress().getCountry(),
                        warehouse.getAddress().getCity()
                )
        );
    }

    public WarehouseUpdateResponseDTO updateWarehouse(@Valid WarehouseUpdateRequestDTO requestDTO) {
        Warehouse warehouse = warehouseModifyingService.updateWarehouse(requestDTO);

        return new WarehouseUpdateResponseDTO(
                warehouse.getId(),
                warehouse.getName(),
                new WarehouseUpdateResponseDTO.AddressDTO(
                        warehouse.getAddress().getAddress(),
                        warehouse.getAddress().getMailIndex(),
                        warehouse.getAddress().getCountry(),
                        warehouse.getAddress().getCity()
                )
        );
    }

    public PageResponseDTO<WarehouseListResponseDTO> getWarehouseList(@Valid RequestDTO request)  {
        Assert.notNull(request, "request must be not null");

        List<Sort.Order> orders = new ArrayList<>();

        orders.add(new Sort.Order(request.sortDTO().direction(), request.sortDTO().field()));

        PageRequest pageRequest = PageRequest.of(
                request.paginationDTO().page(),
                request.paginationDTO().size(),
                Sort.by(orders)
        );

        Specification<@NotNull Warehouse> specification = WarehouseSpecifications.ListSpecification.getSpecification(request.filtersDTO());

        Page<@NotNull Warehouse> warehousePage = warehouseRepository.findAll(specification, pageRequest);

        return PageResponseDTO.from(warehousePage.map(warehouse -> new WarehouseListResponseDTO(
                warehouse.getId(),
                warehouse.getName(),
                new WarehouseListResponseDTO.AddressDTO(
                        warehouse.getAddress().getAddress(),
                        warehouse.getAddress().getMailIndex(),
                        warehouse.getAddress().getCountry(),
                        warehouse.getAddress().getCity()
                )
        )));
    }
}
