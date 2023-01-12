package tt.authorization.entity.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

//@Entity
//@Table(name = "session_object")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionObject {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private CustomSession customSession;
}
