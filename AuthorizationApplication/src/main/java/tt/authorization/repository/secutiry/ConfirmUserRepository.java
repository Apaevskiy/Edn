package tt.authorization.repository.secutiry;

import org.springframework.data.jpa.repository.JpaRepository;
import tt.authorization.entity.security.ConfirmUser;

public interface ConfirmUserRepository extends JpaRepository<ConfirmUser, Long> {
    ConfirmUser findConfirmUserByEmail(String email);
}
