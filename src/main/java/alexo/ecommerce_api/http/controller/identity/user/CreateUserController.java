package alexo.ecommerce_api.http.controller.identity.user;

import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.entity.identity.User;
import alexo.ecommerce_api.service.identity.user.creation.UserCreationService;
import alexo.ecommerce_api.service.identity.user.creation.dto.UserCreationRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
public class CreateUserController {

    private UserCreationService userCreationService;

    @PostMapping("/api/identity/user")
    public User createUser() {

        String email = "dsfkljhlskjdfh@gmail.com";
        String firstName = "dsfkljhlskjdfh";
        String lastName = "dsfkljhlskjdfh";
        String password = "password";

        ArrayList<RoleCode> roleCodesAdditional = new ArrayList<>();
        roleCodesAdditional.add(RoleCode.ADMIN);

        ArrayList<PermissionCode> permissionCodesAdditional = new ArrayList<>();

        UserCreationRequestDTO creationRequestDTO = new UserCreationRequestDTO(
                email,
                firstName,
                lastName,
                password,
                roleCodesAdditional,
                permissionCodesAdditional
        );

        System.out.println(creationRequestDTO);

        return userCreationService.createUser(creationRequestDTO);
    }
}
