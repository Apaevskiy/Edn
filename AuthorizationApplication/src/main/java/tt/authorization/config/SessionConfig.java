package tt.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import tt.authorization.repository.session.SessionObjectRepository;
import tt.authorization.repository.session.SessionRepository;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableSpringHttpSession
public class SessionConfig {
    private final SessionRepository sessionRepository;
private final SessionObjectRepository sessionObjectRepository;

    public SessionConfig(SessionRepository sessionRepository, SessionObjectRepository sessionObjectRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionObjectRepository = sessionObjectRepository;
    }

    /*@Bean
    public ReactiveSessionRepository reactiveSessionRepository() {
        return new CustomSessionRepository(sessionRepository, sessionObjectRepository, new ConcurrentHashMap<>());
    }*/
   /* @Bean
    public ReactiveSessionRepository reactiveSessionRepository() {
        return new ReactiveMapSessionRepository(new ConcurrentHashMap<>());
    }*/
    @Bean
    public org.springframework.session.SessionRepository test1(){
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }
}
