package alexo.ecommerce_api.service.identity.auth.login;

import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.entity.enums.UserStatusCode;
import alexo.ecommerce_api.entity.identity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {
    public static final String PERMISSION_GRANTED_AUTHORITY_PREFIX = "PERMISSION_";
    public static final String ROLE_GRANTED_AUTHORITY_PREFIX = "ROLE_";

    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final List<RoleCode> roles;
    private final List<PermissionCode> permissions;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(
            Long id,
            String username,
            String password,
            boolean enabled,
            List<RoleCode> roles,
            List<PermissionCode> permissions,
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

    public static UserPrincipal from(User user, List<RoleCode> roles, List<PermissionCode> permissions) {
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
                && user.getStatusType().getCode() == UserStatusCode.ACTIVE;

        return new UserPrincipal(
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
