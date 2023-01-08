package tt.authorization.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "requests_join_to_org")
@NoArgsConstructor
@Getter
@Setter
public class RequestJoinToOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;
}
