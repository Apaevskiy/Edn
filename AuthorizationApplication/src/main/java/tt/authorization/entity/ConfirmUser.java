package tt.authorization.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

@Entity
@Table(name = "confirm_user")
@NoArgsConstructor
@Getter
public class ConfirmUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String email;
    private Long dateRegistration;
    private String key;

    public ConfirmUser(String email) {
        this.email = email;
        this.dateRegistration = new Date().getTime();
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        this.key = new String(array, StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
    }
}
