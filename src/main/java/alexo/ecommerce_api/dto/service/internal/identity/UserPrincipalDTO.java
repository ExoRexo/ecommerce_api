package alexo.ecommerce_api.dto.service.internal.identity;

import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.UserStatusType;
import alexo.ecommerce_api.entity.identity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@Getter
public class UserPrincipalDTO implements UserDetails {
    public static final String PERMISSION_GRANTED_AUTHORITY_PREFIX = "PERMISSION_";
    public static final String ROLE_GRANTED_AUTHORITY_PREFIX = "ROLE_";

    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final List<Role.RoleCode> roles;
    private final List<Permission.PermissionCode> permissions;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipalDTO(
            Long id,
            String username,
            String password,
            boolean enabled,
            List<Role.RoleCode> roles,
            List<Permission.PermissionCode> permissions,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.permissions = permissions;
        this.authorities = authorities;
    }

    public static UserPrincipalDTO from(User user, List<Role.RoleCode> roles, List<Permission.PermissionCode> permissions) {
        LinkedHashSet<GrantedAuthority> authorities = new LinkedHashSet<>();

        roles.stream()
                .map((role) -> ROLE_GRANTED_AUTHORITY_PREFIX + role.getCode())
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        permissions.stream()
                .map((role) -> PERMISSION_GRANTED_AUTHORITY_PREFIX + role.getCode())
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        boolean enabled = user.getStatusType() != null
                && user.getStatusType().getCode() == UserStatusType.UserStatusCode.ACTIVE;

        return new UserPrincipalDTO(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                enabled,
                roles,
                permissions,
                authorities
        );
    }
}
