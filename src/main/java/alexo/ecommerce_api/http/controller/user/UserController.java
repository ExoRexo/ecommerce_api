package alexo.ecommerce_api.http.controller.user;

import alexo.ecommerce_api.service.identity.auth.login.UserPrincipal;
import alexo.ecommerce_api.service.identity.user.dto.MePermissionsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
final public class UserController {
    @GetMapping("/me/permissions")
    public MePermissionsResponseDTO getPermissions(@NotNull Authentication authentication) throws AuthenticationException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (userPrincipal == null) {
            throw new AuthenticationCredentialsNotFoundException("permissions not found, probably, your token is invalid");
        }

        return new MePermissionsResponseDTO(
                userPrincipal.getRoles(),
                userPrincipal.getPermissions()
        );
    }

}
