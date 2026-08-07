package alexo.ecommerce_api.repository.identity.user;

import alexo.ecommerce_api.entity.identity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPrincipalRepository extends UserRepository {

    @Override
    @EntityGraph(value = "statusType")
    Optional<User> findByEmail(String email);
}
