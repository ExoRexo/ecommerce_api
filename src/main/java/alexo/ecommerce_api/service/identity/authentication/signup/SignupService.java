package alexo.ecommerce_api.service.identity.authentication.signup;

import alexo.ecommerce_api.entity.customer.Customer;
import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.entity.enums.UserStatusCode;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.entity.identity.UserStatusType;
import alexo.ecommerce_api.repository.customer.CustomerRepository;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.service.identity.authority.UserAuthorityService;
import alexo.ecommerce_api.cache.identity.status.UserStatusCacheService;
import alexo.ecommerce_api.service.identity.authentication.signup.dto.UserCreationDTO;
import alexo.ecommerce_api.service.identity.authentication.signup.dto.request.UserCreationRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SignupService {
    private final UserRepository userRepository;
    private final UserAuthorityService userAuthorityService;
    private final UserStatusCacheService userStatusCacheService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(
        @NotNull UserCreationRequestDTO userCreationRequestDTO,
        @Nullable ArrayList<RoleCode> roleCodesAdditional,
        @Nullable ArrayList<PermissionCode> permissionCodesAdditional
    )
    {
        if (userRepository.existsByEmail(userCreationRequestDTO.email())) {
            throw new IllegalArgumentException("Пользователь с почтой " + userCreationRequestDTO.email() + " уже существует");
        }

        UserStatusType userStatusType = Objects.requireNonNull(userStatusCacheService.getStatusTypes().get(UserStatusCode.ACTIVE));

        Role customerRole = Objects.requireNonNull(userAuthorityService.getRoleByCode(RoleCode.CUSTOMER));

        HashSet<Role> roleHashSet = new HashSet<>();
        roleHashSet.add(customerRole);

        if (roleCodesAdditional != null && !roleCodesAdditional.isEmpty()) {
            HashSet<Role> additionalRoles = new HashSet<>();

            for (RoleCode roleCode : roleCodesAdditional) {
                Role additionalRole = Objects.requireNonNull(userAuthorityService.getRoleByCode(roleCode));

                additionalRoles.add(additionalRole);
            }

            roleHashSet.addAll(additionalRoles);
        }

        HashSet<Permission> permissionHashSet = new HashSet<>(10);
        if (permissionCodesAdditional != null && !permissionCodesAdditional.isEmpty()) {
            HashSet<Permission> additionalPermissions = new HashSet<>(permissionCodesAdditional.size());

            for (PermissionCode permissionCode : permissionCodesAdditional) {
                Permission additionalPermission = userAuthorityService.getPermissionByCode(permissionCode);

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
    private User persistUserEntity(@NotNull UserCreationDTO userCreationDTO)
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
        userAuthorityService.replaceUserRoles(user, roles);

        ArrayList<Permission> permissions = new ArrayList<>(userCreationDTO.directPermissions().size());
        permissions.addAll(userCreationDTO.directPermissions());
        userAuthorityService.replaceUserDirectPermissions(user, permissions);

        Customer customer = new Customer();
        customer.setUser(user);
        customerRepository.save(customer);

        return user;
    }
}
