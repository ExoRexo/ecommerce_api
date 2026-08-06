package alexo.ecommerce_api.http.controller.auth;

import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.service.identity.user.creation.UserCreationService;
import alexo.ecommerce_api.service.identity.user.creation.dto.UserCreationRequestDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private UserCreationService userCreationService;

    @PostMapping("/signup")
    public User createUser(@Valid @RequestBody UserCreationRequestDTO creationRequestDTO) {
        return userCreationService.createUser(creationRequestDTO, null, null);
    }
}
