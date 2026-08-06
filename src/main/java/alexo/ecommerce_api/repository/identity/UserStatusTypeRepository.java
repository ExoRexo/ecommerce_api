package alexo.ecommerce_api.repository.identity;

import alexo.ecommerce_api.entity.identity.UserStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusTypeRepository extends JpaRepository<UserStatusType, Short> {
}

