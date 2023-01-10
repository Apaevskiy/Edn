package tt.authorization.entity.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

@Entity
@Table(name = "confirm_user")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConfirmUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String email;
    private Long dateRegistration;
    private String key;
    private boolean isConfirm;

    public ConfirmUser(String email) {
        this.email = email;
        this.dateRegistration = new Date().getTime();

        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 6;
        Random random = new Random();

        this.key = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();
    }
}
