package tt.authorization.repository.session;

import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import reactor.core.publisher.Mono;
import tt.authorization.entity.session.CustomSession;

import javax.transaction.Transactional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Transactional
public class CustomSessionRepository implements ReactiveSessionRepository<CustomSession> {

    private final SessionRepository sessionRepository;

    public CustomSessionRepository(SessionRepository sessionRepository,
                                   ConcurrentHashMap<String, Session> map) {
        this.sessionRepository = sessionRepository;
        if (map != null)
            sessionRepository.saveAll(map.values().stream().map(CustomSession::new).collect(Collectors.toList()));
    }

    @Override
    public Mono<CustomSession> createSession() {
        return Mono.just(new CustomSession());
    }

    @Override
    public Mono<Void> save(CustomSession session) {
        if (!session.getId().equals(session.getOriginalId())) {
            sessionRepository.deleteByName(session.getOriginalId());
        }
        try {
            sessionRepository.save(new CustomSession(session));
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
        }
        return Mono.empty();
    }

    @Override
    public Mono<CustomSession> findById(String name) {
        return Mono.justOrEmpty(sessionRepository.findFirstByName(name))
                .filter((session) -> !session.isExpired())
                .switchIfEmpty(this.deleteById(name).then(Mono.empty()));
    }

    @Override
    public Mono<Void> deleteById(String name) {
        sessionRepository.deleteByName(name);
        return Mono.empty();
    }
}
