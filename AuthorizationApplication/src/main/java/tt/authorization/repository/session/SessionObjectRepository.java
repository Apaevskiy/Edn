package tt.authorization.repository.session;

import org.springframework.data.jpa.repository.JpaRepository;
import tt.authorization.entity.session.SessionObject;

public interface SessionObjectRepository extends JpaRepository<SessionObject, String> {
}
