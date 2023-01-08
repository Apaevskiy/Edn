package tt.authorization.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tt.authorization.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles"})
    User findByUsername(String username);

    @EntityGraph(attributePaths = {"roles"})
    User findFirstById(Long id);
}
