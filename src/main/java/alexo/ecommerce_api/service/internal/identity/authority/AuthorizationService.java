package alexo.ecommerce_api.service.internal.identity.authority;

import alexo.ecommerce_api.dto.service.identity.UserPrincipalDTO;
import alexo.ecommerce_api.enums.entity.PermissionCode;
import alexo.ecommerce_api.enums.entity.RoleCode;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
final public class AuthorizationService {

    private static final String ROLE_AUTHORITY_PREFIX = UserPrincipalDTO.ROLE_GRANTED_AUTHORITY_PREFIX;
    private static final String PERMISSION_AUTHORITY_PREFIX = UserPrincipalDTO.PERMISSION_GRANTED_AUTHORITY_PREFIX;

    public boolean hasRoleAuthority(RoleCode roleCode) {
        return hasCodeAuthority(ROLE_AUTHORITY_PREFIX + roleCode.getCode());
    }

    public boolean hasPermissionAuthority(PermissionCode permissionCode) {
        return hasCodeAuthority(PERMISSION_AUTHORITY_PREFIX + permissionCode.getCode());
    }

    private boolean hasCodeAuthority(String authorityWithPrefix) {
        Authentication authentication = getAuthentication();

        return authentication != null
                && authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        authorityWithPrefix.equals(a.getAuthority()));
    }

    private @Nullable Authentication getAuthentication() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }
}
