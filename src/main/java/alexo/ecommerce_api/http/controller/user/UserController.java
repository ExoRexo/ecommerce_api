package alexo.ecommerce_api.http.controller.user;

import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.repository.identity.user.UserRepository;
import alexo.ecommerce_api.service.identity.auth.login.UserPrincipal;
import alexo.ecommerce_api.service.identity.user.dto.MeAuthoritiesResponseDTO;
import alexo.ecommerce_api.service.identity.user.dto.profile.ProfileResponseDTO;
import alexo.ecommerce_api.service.identity.user.dto.profile.StatusTypeDTO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private UserRepository userRepository;

    @GetMapping("/me/authorities")
    public MeAuthoritiesResponseDTO getAuthorities(@NotNull Authentication authentication) throws AuthenticationException {
        if (!(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
            throw new AuthenticationCredentialsNotFoundException("authentication not found, probably, your token is invalid");
        }

        return new MeAuthoritiesResponseDTO(
                userPrincipal.getRoles(),
                userPrincipal.getPermissions()
        );
    }

    @GetMapping("/me/profile")
    @Transactional
    public ProfileResponseDTO getProfile(@NotNull Authentication authentication) throws AuthenticationException {
        if (!(authentication.getPrincipal() instanceof UserPrincipal userPrincipal) || userPrincipal.getId() == null) {
            throw new AuthenticationCredentialsNotFoundException("authentication not found, probably, your token is invalid");
        }

        User user = userRepository
                .findByIdForUserProfile(userPrincipal.getId())
                .orElseThrow();

        return new ProfileResponseDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                new StatusTypeDTO(
                        user.getStatusType().getCode(),
                        user.getStatusType().getLabel(),
                        user.getStatusType().getDescription()
                )
        );
    }

}
