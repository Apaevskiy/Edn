package tt.authorization.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organizations")
@NoArgsConstructor
@Getter
@Setter
public class Organization {
    @Id
    private Long id;    // УНП
    private String name;
    private String alternativeName;
}
