package alexo.ecommerce_api.repository.identity;

import alexo.ecommerce_api.entity.identity.UserPermission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    @Modifying
    @Query("delete from UserPermission up where up.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @EntityGraph("permission")
    List<UserPermission> findByPermissionIdInAndUserId(List<Long> permissionId, Long userId);

    @Modifying
    @Query("delete from UserPermission up where up.user.id = :userId and up.permission.id in (:permissionId)")
    void deleteAllByUserIdAndPermissionIdIn(@Param("userId") Long userId, @Param("permissionId") List<Long> permissionId);
}

