package alexo.ecommerce_api.http.controller.inventory.warehouse;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.request.WarehouseCreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.inventory.warehuose.create.response.WarehouseCreateResponseDTO;
import alexo.ecommerce_api.service.internal.inventory.warehouse.WarehouseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inventory/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_INVENTORY_WAREHOUSE_CREATE')") // todo
    public ResponseEntity<@NotNull ApiResponseDTO<WarehouseCreateResponseDTO>> createWarehouse(@Valid @RequestBody WarehouseCreateRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(warehouseService.createWarehouse(requestDTO)));
    }
}
