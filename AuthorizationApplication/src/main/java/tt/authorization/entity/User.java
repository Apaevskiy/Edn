package tt.authorization.entity;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Email(regexp = ".+[@].+[\\.].+", message = "Неверный формат почты")
    private String username;
    @Column(columnDefinition = "varchar(255) default '$2a$10$EjvY.OfCwyFqpgj03sR5D.LoJm2R53KvbYrhmC1D.BtrWbla77OEW'")
    @NotBlank(message = "Пароль не должен быть пустым")
    @Expose
    private String password;
    @Transient
    private String confirmPassword;
    private String activationCode;
    private boolean active;
    private Long dateRegistration;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(getRole());
    }

    @Override
    public String getPassword() {
        return password;
    }

}
