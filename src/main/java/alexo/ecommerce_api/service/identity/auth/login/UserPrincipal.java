package alexo.ecommerce_api.service.identity.auth.login;

import alexo.ecommerce_api.entity.enums.UserStatusCode;
import alexo.ecommerce_api.entity.identity.User;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserPrincipal implements UserDetails {
    public static final String PERMISSION_GRANTED_AUTHORITY_PREFIX = "PERMISSION_";
    public static final String ROLE_GRANTED_AUTHORITY_PREFIX = "ROLE_";

    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final List<String> roles;
    private final List<String> permissions;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(
            Long id,
            String username,
            String password,
            boolean enabled,
            List<String> roles,
            List<String> permissions,
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

    public static UserPrincipal from(User user, List<String> roles, List<String> permissions) {
        LinkedHashSet<GrantedAuthority> authorities = new LinkedHashSet<>();

        roles.stream()
                .map((role) -> ROLE_GRANTED_AUTHORITY_PREFIX + role)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        permissions.stream()
                .map((role) -> PERMISSION_GRANTED_AUTHORITY_PREFIX + role)
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
