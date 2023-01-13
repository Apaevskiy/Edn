package tt.authorization.repository.secutiry;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tt.authorization.entity.security.User;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"role"})
    User findByUsername(String username);

    @EntityGraph(attributePaths = {"role"})
    User findFirstById(Long id);

    User findByActivationCode(String key);

    @Query(value =
            "select u,r from User u " +
                    "left join u.role r " +
                    "where (:email is null or :email <> '' or u.username = :email) and " +
                    "(:roleId is null or r.id = :roleId)")
    @EntityGraph(attributePaths = {"role"})
    Set<User> findAllAndFilterByEmailAndRole(@Param("email") String email, @Param("roleId") Long roleId);
}
