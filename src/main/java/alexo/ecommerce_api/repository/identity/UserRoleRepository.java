package alexo.ecommerce_api.repository.identity;

import alexo.ecommerce_api.entity.identity.Role;
import alexo.ecommerce_api.entity.identity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query(
    """
        select ur.role
        from UserRole ur
        where ur.user.id = :userId
    """
    )
    List<Role> findRolesByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from UserRole up where up.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}

