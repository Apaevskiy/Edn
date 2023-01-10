package tt.authorization.entity.session;

import org.springframework.session.Session;
import org.springframework.util.SerializationUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "sessions")
public class CustomSession implements Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private final String originalName;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "session_attributes", joinColumns=@JoinColumn(name="session_id"))
    @MapKeyColumn(name = "attr_name")
    @Column(name = "attr_value")
    private Map<String, byte[]> sessionAttrs;
    private Timestamp creationTime;
    private Timestamp lastAccessedTime;
    private Duration maxInactiveInterval;

    public CustomSession() {
        this(generateName());
    }

    public CustomSession(String name) {
        this.sessionAttrs = new HashMap<>();
        this.creationTime = Timestamp.from(Instant.now());
        this.lastAccessedTime = this.creationTime;
        this.maxInactiveInterval = Duration.ofSeconds(1800L);
        this.name = name;
        this.originalName = name;
    }

    public CustomSession(Session session) {
        this.sessionAttrs = new HashMap<>();
        this.creationTime = Timestamp.from(Instant.now());
        this.lastAccessedTime = this.creationTime;
        this.maxInactiveInterval = Duration.ofSeconds(1800L);
        if (session == null) {
            throw new IllegalArgumentException("session cannot be null");
        } else {
            this.name = session.getId();
            this.originalName = this.name;
            this.sessionAttrs = new HashMap(session.getAttributeNames().size());
            Iterator var2 = session.getAttributeNames().iterator();

            while(var2.hasNext()) {
                String attrName = (String)var2.next();
                Object attrValue = session.getAttribute(attrName);
                if (attrValue != null) {
                    this.sessionAttrs.put(attrName, SerializationUtils.serialize(attrValue));
                }
            }

            this.lastAccessedTime = Timestamp.from(session.getLastAccessedTime());
            this.creationTime = Timestamp.from(session.getCreationTime());
            this.maxInactiveInterval = session.getMaxInactiveInterval();
        }
    }

    public void setLastAccessedTime(Instant lastAccessedTime) {
        this.lastAccessedTime = Timestamp.from(lastAccessedTime);
    }

    public Instant getCreationTime() {
        return this.creationTime.toInstant();
    }

    public String getId() {
        return this.name;
    }

    public String getOriginalId() {
        return this.originalName;
    }

    public String changeSessionId() {
        String changedId = generateName();
        this.setId(changedId);
        return changedId;
    }

    public Instant getLastAccessedTime() {
        return this.lastAccessedTime.toInstant();
    }

    public void setMaxInactiveInterval(Duration interval) {
        this.maxInactiveInterval = interval;
    }

    public Duration getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    public boolean isExpired() {
        return this.isExpired(Instant.now());
    }

    boolean isExpired(Instant now) {
        if (this.maxInactiveInterval.isNegative()) {
            return false;
        } else {
            return now.minus(this.maxInactiveInterval).compareTo(this.lastAccessedTime.toInstant()) >= 0;
        }
    }

    public <T> T getAttribute(String attributeName) {
        return (T) SerializationUtils.deserialize(sessionAttrs.get(attributeName));
    }

    public Set<String> getAttributeNames() {
        return new HashSet(this.sessionAttrs.keySet());
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            this.removeAttribute(attributeName);
        } else {
            this.sessionAttrs.put(attributeName, SerializationUtils.serialize(attributeValue));
        }

    }

    public void removeAttribute(String attributeName) {
        this.sessionAttrs.remove(attributeName);
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = Timestamp.from(creationTime);
    }

    public void setId(String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        return obj instanceof Session && this.name.equals(((Session)obj).getId());
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    private static String generateName() {
        return UUID.randomUUID().toString();
    }
}
