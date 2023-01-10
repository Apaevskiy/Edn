package tt.authorization.repository.session;

import org.springframework.data.jpa.repository.JpaRepository;
import tt.authorization.entity.session.CustomSession;

public interface SessionRepository extends JpaRepository<CustomSession, Long> {
    CustomSession findFirstByName(String name);

    long deleteByName(String name);
}
