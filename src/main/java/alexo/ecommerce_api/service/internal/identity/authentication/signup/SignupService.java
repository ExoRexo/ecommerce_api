package alexo.ecommerce_api.service.internal.identity.authentication.signup;

import alexo.ecommerce_api.entity.customer.Customer;
import alexo.ecommerce_api.entity.customer.cart.CustomerCart;
import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.entity.identity.UserStatusType;
import alexo.ecommerce_api.repository.customer.CustomerCartRepository;
import alexo.ecommerce_api.repository.customer.CustomerRepository;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.service.internal.identity.authority.UserAuthorityService;
import alexo.ecommerce_api.cache.identity.status.UserStatusCacheService;
import alexo.ecommerce_api.dto.service.internal.identity.authentication.signup.UserCreationDTO;
import alexo.ecommerce_api.dto.service.internal.identity.authentication.signup.request.UserSignupRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
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
    private final CustomerCartRepository customerCartRepository;

    @Transactional
    public User createUser(
        @NotNull UserSignupRequestDTO userSignupRequestDTO,
        @Nullable ArrayList<Role.RoleCode> roleCodesAdditional,
        @Nullable ArrayList<Permission.PermissionCode> permissionCodesAdditional
    )
    {
        if (userRepository.existsByEmail(userSignupRequestDTO.email())) {
            throw new IllegalArgumentException("Пользователь с почтой " + userSignupRequestDTO.email() + " уже существует");
        }

        UserStatusType userStatusType = Objects.requireNonNull(userStatusCacheService.getStatusTypes().get(UserStatusType.UserStatusCode.ACTIVE));

        Role customerRole = Objects.requireNonNull(userAuthorityService.getRoleByCode(Role.RoleCode.CUSTOMER));

        HashSet<Role> roleHashSet = new HashSet<>();
        roleHashSet.add(customerRole);

        if (roleCodesAdditional != null && !roleCodesAdditional.isEmpty()) {
            HashSet<Role> additionalRoles = new HashSet<>();

            for (Role.RoleCode roleCode : roleCodesAdditional) {
                Role additionalRole = Objects.requireNonNull(userAuthorityService.getRoleByCode(roleCode));

                additionalRoles.add(additionalRole);
            }

            roleHashSet.addAll(additionalRoles);
        }

        HashSet<Permission> permissionHashSet = new HashSet<>(10);
        if (permissionCodesAdditional != null && !permissionCodesAdditional.isEmpty()) {
            HashSet<Permission> additionalPermissions = new HashSet<>(permissionCodesAdditional.size());

            for (Permission.PermissionCode permissionCode : permissionCodesAdditional) {
                Permission additionalPermission = userAuthorityService.getPermissionByCode(permissionCode);

                if (additionalPermission == null) {
                    throw new EntityNotFoundException("Не найден доступ " + permissionCode.getCode());
                }

                additionalPermissions.add(additionalPermission);
            }

            permissionHashSet.addAll(additionalPermissions);
        }

        String passwordHash = passwordEncoder.encode(userSignupRequestDTO.password());

        UserCreationDTO userCreationDTO = new UserCreationDTO(
                userSignupRequestDTO.email(),
                userSignupRequestDTO.firstName(),
                userSignupRequestDTO.lastName(),
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

        customerCartRepository.save(CustomerCart.builder().customer(customer).build());

        return user;
    }
}
