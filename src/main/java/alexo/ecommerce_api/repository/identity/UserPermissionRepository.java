package alexo.ecommerce_api.repository.identity;

import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    @Query("select distinct up.permission from UserPermission up where up.user.id = :userId")
    Set<Permission> findDirectPermissionsByUserId(@Param("userId") Long userId);
}

