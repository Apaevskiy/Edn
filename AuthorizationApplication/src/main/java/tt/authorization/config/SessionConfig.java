package tt.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import tt.authorization.repository.session.CustomSessionRepository;
import tt.authorization.repository.session.SessionRepository;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableSpringWebSession
public class SessionConfig {
    private final SessionRepository sessionRepository;

    public SessionConfig(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Bean
    public ReactiveSessionRepository reactiveSessionRepository() {
        return new CustomSessionRepository(sessionRepository, new ConcurrentHashMap<>());
    }
}
