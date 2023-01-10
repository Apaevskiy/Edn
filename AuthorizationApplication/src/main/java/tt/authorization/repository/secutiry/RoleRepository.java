package tt.authorization.repository.secutiry;


import org.springframework.data.jpa.repository.JpaRepository;
import tt.authorization.entity.security.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
}
