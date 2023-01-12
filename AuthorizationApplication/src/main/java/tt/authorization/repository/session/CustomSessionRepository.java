package tt.authorization.repository.session;

import org.springframework.session.MapSession;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Map;

@Transactional
public class CustomSessionRepository implements ReactiveSessionRepository<MapSession> {

    /*private final SessionRepository sessionRepository;
    private final SessionObjectRepository sessionObjectRepository;

    public CustomSessionRepository(SessionRepository sessionRepository,
                                   SessionObjectRepository sessionObjectRepository,
                                   ConcurrentHashMap<String, Session> map) {
        this.sessionRepository = sessionRepository;
        this.sessionObjectRepository = sessionObjectRepository;
        if (map != null && !map.isEmpty())
            sessionRepository.saveAll(map.values().stream().map(CustomSession::new).collect(Collectors.toList()));
    }*/
    private Integer defaultMaxInactiveInterval;
    private final Map<String, Session> sessions;
    public CustomSessionRepository(Map<String, Session> sessions) {
        if (sessions == null) {
            throw new IllegalArgumentException("sessions cannot be null");
        } else {
            this.sessions = sessions;
        }
    }

    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    public Mono<Void> save(MapSession session) {
        return Mono.fromRunnable(() -> {
            if (!session.getId().equals(session.getOriginalId())) {
                this.sessions.remove(session.getOriginalId());
            }

            this.sessions.put(session.getId(), new MapSession(session));
        });
    }

    public Mono<MapSession> findById(String id) {
        return Mono.defer(() -> {
            return Mono.justOrEmpty(this.sessions.get(id)).filter((session) -> {
                return !session.isExpired();
            }).map(MapSession::new).switchIfEmpty(this.deleteById(id).then(Mono.empty()));
        });
    }

    public Mono<Void> deleteById(String id) {
        return Mono.fromRunnable(() -> {
            Session var10000 = (Session)this.sessions.remove(id);
        });
    }

    public Mono<MapSession> createSession() {
        return Mono.defer(() -> {
            MapSession result = new MapSession();
            if (this.defaultMaxInactiveInterval != null) {
                result.setMaxInactiveInterval(Duration.ofSeconds((long)this.defaultMaxInactiveInterval));
            }

            return Mono.just(result);
        });
    }

}
