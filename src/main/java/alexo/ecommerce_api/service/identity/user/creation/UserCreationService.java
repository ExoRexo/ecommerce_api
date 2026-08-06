package alexo.ecommerce_api.service.identity.user.creation;

import alexo.ecommerce_api.entity.customer.Customer;
import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.entity.enums.UserStatusCode;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.entity.identity.UserStatusType;
import alexo.ecommerce_api.repository.customer.CustomerRepository;
import alexo.ecommerce_api.repository.identity.UserRepository;
import alexo.ecommerce_api.service.identity.permission.UserPermissionService;
import alexo.ecommerce_api.service.identity.status.StatusCacheService;
import alexo.ecommerce_api.service.identity.user.creation.dto.UserCreationDTO;
import alexo.ecommerce_api.service.identity.user.creation.dto.UserCreationRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class UserCreationService {
    private final UserRepository userRepository;
    private final UserPermissionService userPermissionService;
    private final StatusCacheService statusCacheService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(
        UserCreationRequestDTO userCreationRequestDTO
    )
    {
        if (userCreationRequestDTO == null) {
            throw new IllegalArgumentException("userCreationRequestDTO cannot be null");
        }

        if (userRepository.existsByEmail(userCreationRequestDTO.email())) {
            throw new IllegalArgumentException("Пользователь с почтой " + userCreationRequestDTO.email() + " уже существует");
        }

        UserStatusType userStatusType = statusCacheService.getStatusTypes().get(UserStatusCode.ACTIVE);
        if (userStatusType == null) {
            throw new EntityNotFoundException("Не найден статус ACTIVE");
        }

        Role customerRole = userPermissionService.getRoleByCode(RoleCode.CUSTOMER);
        if (customerRole == null) {
            throw new EntityNotFoundException("Не найдена роль CUSTOMER");
        }

        HashSet<Role> roleHashSet = new HashSet<>(5);
        roleHashSet.add(customerRole);

        if (!userCreationRequestDTO.roleCodesAdditional().isEmpty()) {
            HashSet<Role> additionalRoles = new HashSet<>(userCreationRequestDTO.roleCodesAdditional().size());

            for (RoleCode roleCode : userCreationRequestDTO.roleCodesAdditional()) {
                Role additionalRole = userPermissionService.getRoleByCode(roleCode);

                if (additionalRole == null) {
                    throw new EntityNotFoundException("Не найдена роль " + roleCode.getCode());
                }

                additionalRoles.add(additionalRole);
            }

            roleHashSet.addAll(additionalRoles);
        }

        HashSet<Permission> permissionHashSet = new HashSet<>(10);
        if (!userCreationRequestDTO.permissionCodesAdditional().isEmpty()) {
            HashSet<Permission> additionalPermissions = new HashSet<>(userCreationRequestDTO.permissionCodesAdditional().size());

            for (PermissionCode permissionCode : userCreationRequestDTO.permissionCodesAdditional()) {
                Permission additionalPermission = userPermissionService.getPermissionByCode(permissionCode);

                if (additionalPermission == null) {
                    throw new EntityNotFoundException("Не найден доступ " + permissionCode.getCode());
                }

                additionalPermissions.add(additionalPermission);
            }

            permissionHashSet.addAll(additionalPermissions);
        }

        String passwordHash = passwordEncoder.encode(userCreationRequestDTO.password());

        UserCreationDTO userCreationDTO = new UserCreationDTO(
                userCreationRequestDTO.email(),
                userCreationRequestDTO.firstName(),
                userCreationRequestDTO.lastName(),
                userStatusType,
                roleHashSet,
                permissionHashSet,
                passwordHash
        );

        return persistUserEntity(userCreationDTO);
    }

    /**
     * @param userCreationDTO dto to create user
     * @return user entity
     */
    protected User persistUserEntity(@NotNull UserCreationDTO userCreationDTO)
    {
        User user = new User();

        user.setEmail(userCreationDTO.email());
        user.setFirstName(userCreationDTO.firstName());
        user.setLastName(userCreationDTO.lastName());
        user.setStatusType(userCreationDTO.statusType());
        user.setPasswordHash(userCreationDTO.passwordHash());
        user = userRepository.save(user);

        ArrayList<Role> roles = new ArrayList<>(userCreationDTO.roles().size());
        roles.addAll(userCreationDTO.roles());
        userPermissionService.replaceUserRoles(user, roles);

        ArrayList<Permission> permissions = new ArrayList<>(userCreationDTO.directPermissions().size());
        permissions.addAll(userCreationDTO.directPermissions());
        userPermissionService.replaceUserDirectPermissions(user, permissions);

        Customer customer = new Customer();
        customer.setUser(user);
        customerRepository.save(customer);

        return user;
    }
}
