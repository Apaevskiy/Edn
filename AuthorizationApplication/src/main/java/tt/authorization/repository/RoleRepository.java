package tt.authorization.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tt.authorization.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findFirstByName(String role_admin);
}
