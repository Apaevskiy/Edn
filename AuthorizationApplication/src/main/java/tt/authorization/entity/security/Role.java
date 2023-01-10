package tt.authorization.entity.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role implements GrantedAuthority {
    @Id
    private Long id;
    private String name;
    private String fullName;

    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public String toString() {
        return name.replaceAll("ROLE_","");
    }
}
