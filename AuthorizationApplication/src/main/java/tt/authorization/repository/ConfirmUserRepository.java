package tt.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tt.authorization.entity.ConfirmUser;

public interface ConfirmUserRepository extends JpaRepository<ConfirmUser, Long> {
    ConfirmUser findConfirmUserByEmail(String email);
}
