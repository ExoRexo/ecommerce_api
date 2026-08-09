package alexo.ecommerce_api.repository.identity.user;

import alexo.ecommerce_api.entity.identity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {
            "directPermissions",
            "roles.permissions",
            "statusType",
    })
    @Query("""
        select u
        from User u
        where u.id = :id
    """)
    Optional<User> findByIdForUserDetails(Long id);

    @EntityGraph(attributePaths = {
            "statusType",
    })
    @Query("""
        select u
        from User u
        where u.id = :id
    """)
    Optional<User> findByIdForUserProfile(Long id);

    @EntityGraph(attributePaths = {
            "directPermissions",
            "roles.permissions",
            "statusType",
    })
    @Query("""
        select u
        from User u
        where u.email = :email
    """)
    Optional<User> findByEmailForUserDetails(String email);

    @Query("""
        select u.email, u.passwordHash
        from User u
        where u.email = :email
    """)
    Optional<User> findByEmailForUserDetailsBuild(String email);
}

