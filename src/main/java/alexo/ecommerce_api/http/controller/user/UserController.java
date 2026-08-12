package alexo.ecommerce_api.http.controller.user;

import alexo.ecommerce_api.dto.service.identity.UserPrincipalDTO;
import alexo.ecommerce_api.dto.service.identity.user.MeAuthoritiesResponseDTO;
import alexo.ecommerce_api.dto.service.identity.user.permissions.AddUserPermissionsRequestDTO;
import alexo.ecommerce_api.dto.service.identity.user.permissions.RemoveUserPermissionsRequestDTO;
import alexo.ecommerce_api.dto.service.identity.user.profile.MeProfileResponseDTO;
import alexo.ecommerce_api.dto.service.identity.user.profile.StatusTypeDTO;
import alexo.ecommerce_api.dto.service.identity.user.permissions.ReplaceUserPermissionsRequestDTO;
import alexo.ecommerce_api.dto.service.identity.user.roles.AddUserRolesRequestDTO;
import alexo.ecommerce_api.dto.service.identity.user.roles.RemoveUserRolesRequestDTO;
import alexo.ecommerce_api.dto.service.identity.user.roles.ReplaceUserRolesRequestDTO;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.service.internal.identity.authority.UserAuthorityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private UserRepository userRepository;
    private UserAuthorityService userAuthorityService;

    @GetMapping("/me/authorities")
    public ResponseEntity<@NotNull ApiResponseDTO<MeAuthoritiesResponseDTO>> getAuthorities(@NotNull Authentication authentication) throws AuthenticationException {
        if (!(authentication.getPrincipal() instanceof UserPrincipalDTO userPrincipalDTO)) {
            throw new AuthenticationCredentialsNotFoundException("authentication not found, probably, your token is invalid");
        }

        return ResponseEntity.ok(ApiResponseDTO.success(new MeAuthoritiesResponseDTO(
                userPrincipalDTO.getRoles(),
                userPrincipalDTO.getPermissions()
        )));
    }

    @GetMapping("/me/profile")
    @Transactional
    public ResponseEntity<@NotNull ApiResponseDTO<MeProfileResponseDTO>> getProfile(@NotNull Authentication authentication) throws AuthenticationException {
        if (!(authentication.getPrincipal() instanceof UserPrincipalDTO userPrincipalDTO) || userPrincipalDTO.getId() == null) {
            throw new AuthenticationCredentialsNotFoundException("authentication not found, probably, your token is invalid");
        }

        User user = userRepository
                .findByIdForUserProfile(userPrincipalDTO.getId())
                .orElseThrow();

        return ResponseEntity.ok(ApiResponseDTO.success(new MeProfileResponseDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                new StatusTypeDTO(
                        user.getStatusType().getCode(),
                        user.getStatusType().getLabel(),
                        user.getStatusType().getDescription()
                )
        )));
    }

    @PostMapping("/roles/replace")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<@NotNull ApiResponseDTO<Void>> replaceUserRoles(@Valid @RequestBody ReplaceUserRolesRequestDTO request) {
        userAuthorityService.replaceUserRoles(
                userRepository
                        .findById(request.userId())
                        .orElseThrow(),
                request
                        .roleCodes()
                        .stream()
                        .map(role -> Optional
                                .ofNullable(userAuthorityService.getRoleByCode(role))
                                .orElseThrow()
                        )
                        .toList()
        );

        return ResponseEntity.ok(ApiResponseDTO.success());
    }

    @PostMapping("/roles/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<@NotNull ApiResponseDTO<Void>> addUserRoles(@Valid @RequestBody AddUserRolesRequestDTO request) {
        userAuthorityService.addUserRoles(
                userRepository
                        .findById(request.userId())
                        .orElseThrow(),
                request
                        .roleCodes()
                        .stream()
                        .map(role -> Optional
                                .ofNullable(userAuthorityService.getRoleByCode(role))
                                .orElseThrow()
                        )
                        .toList()
        );

        return ResponseEntity.ok(ApiResponseDTO.success());
    }

    @PostMapping("/roles/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<@NotNull ApiResponseDTO<Void>> removeUserRoles(@Valid @RequestBody RemoveUserRolesRequestDTO request) {
        userAuthorityService.removeUserRoles(
                userRepository
                        .findById(request.userId())
                        .orElseThrow(),
                request
                        .roleCodes()
                        .stream()
                        .map(role -> Optional
                                .ofNullable(userAuthorityService.getRoleByCode(role))
                                .orElseThrow()
                        )
                        .toList()
        );

        return ResponseEntity.ok(ApiResponseDTO.success());
    }

    @PostMapping("/permissions/replace")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<@NotNull ApiResponseDTO<Void>> replaceUserPermissions(@Valid @RequestBody ReplaceUserPermissionsRequestDTO request) {
        userAuthorityService.replaceUserDirectPermissions(
                userRepository
                        .findById(request.userId())
                        .orElseThrow(),
                request
                        .permissionCodes()
                        .stream()
                        .map(permission -> Optional
                                .ofNullable(userAuthorityService.getPermissionByCode(permission))
                                .orElseThrow()
                        )
                        .toList()
        );

        return ResponseEntity.ok(ApiResponseDTO.success());
    }

    @PostMapping("/permissions/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<@NotNull ApiResponseDTO<Void>> addUserPermissions(@Valid @RequestBody AddUserPermissionsRequestDTO request) {
        userAuthorityService.addUserDirectPermissions(
                userRepository
                        .findById(request.userId())
                        .orElseThrow(),
                request
                        .permissionCodes()
                        .stream()
                        .map(permission -> Optional
                                .ofNullable(userAuthorityService.getPermissionByCode(permission))
                                .orElseThrow()
                        )
                        .toList()
        );

        return ResponseEntity.ok(ApiResponseDTO.success());
    }

    @PostMapping("/permissions/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<@NotNull ApiResponseDTO<Void>> removeUserPermissions(@Valid @RequestBody RemoveUserPermissionsRequestDTO request) {
        userAuthorityService.removeUserDirectPermissions(
                userRepository
                        .findById(request.userId())
                        .orElseThrow(),
                request
                        .permissionCodes()
                        .stream()
                        .map(permission -> Optional
                                .ofNullable(userAuthorityService.getPermissionByCode(permission))
                                .orElseThrow()
                        )
                        .toList()
        );

        return ResponseEntity.ok(ApiResponseDTO.success());
    }

}
