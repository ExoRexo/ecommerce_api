package alexo.ecommerce_api.repository.identity;

import alexo.ecommerce_api.entity.identity.Permission;
import alexo.ecommerce_api.entity.identity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    @Query("select distinct rp.permission from RolePermission rp where rp.role.id in :roleIds")
    Set<Permission> findPermissionsByRoleIds(@Param("roleIds") Collection<Integer> roleIds);

}

