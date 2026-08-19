package alexo.ecommerce_api.http.controller.inventory.warehouse;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.WarehouseCreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.WarehouseCreateResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.list.RequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.list.WarehouseListResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update.WarehouseUpdateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.update.WarehouseUpdateResponseDTO;
import alexo.ecommerce_api.service.internal.inventory.warehouse.WarehouseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inventory/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_INVENTORY_WAREHOUSE_CREATE')")
    public ResponseEntity<@NotNull ApiResponseDTO<WarehouseCreateResponseDTO>> createWarehouse(@Valid @RequestBody WarehouseCreateRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(warehouseService.createWarehouse(requestDTO)));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_INVENTORY_WAREHOUSE_UPDATE')")
    public ResponseEntity<@NotNull ApiResponseDTO<WarehouseUpdateResponseDTO>> updateWarehouse(@Valid @RequestBody WarehouseUpdateRequestDTO requestDTO) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(warehouseService.updateWarehouse(requestDTO)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_INVENTORY_WAREHOUSE_READ_LIST')")
    public ResponseEntity<@NotNull ApiResponseDTO<PageResponseDTO<WarehouseListResponseDTO>>> getWarehouseList(
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size,
            @RequestParam (name = "id", required = false) Long id,
            @RequestParam (name = "name", required = false) String name,
            @RequestParam (name = "addressAddress", required = false) String addressAddress,
            @RequestParam (name = "addressMailIndex", required = false) String addressMailIndex,
            @RequestParam (name = "addressCountry", required = false) String addressCountry,
            @RequestParam (name = "addressCity", required = false) String addressCity

    ) {
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(warehouseService.getWarehouseList(
                        new RequestDTO(
                                new RequestDTO.SortDTO(
                                        sortField,
                                        sortDirection
                                ),
                                new RequestDTO.PaginationDTO(
                                        page,
                                        size
                                ),
                                new RequestDTO.FiltersDTO(
                                        id,
                                        name,
                                        addressAddress,
                                        addressMailIndex,
                                        addressCountry,
                                        addressCity
                                )
                        )
                )));
    }
}
