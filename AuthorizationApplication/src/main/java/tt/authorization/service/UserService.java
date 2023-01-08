package tt.authorization.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tt.authorization.entity.ConfirmUser;
import tt.authorization.entity.Role;
import tt.authorization.entity.User;
import tt.authorization.repository.ConfirmUserRepository;
import tt.authorization.repository.RoleRepository;
import tt.authorization.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService, ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConfirmUserRepository confirmUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = bCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public List<Role> allRoles() {
        return roleRepository.findAll();
    }

    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        } else {
            User u = userRepository.findFirstById(user.getId());
            if (u == null) {
                return null;
            } else if (!u.getPassword().equals(user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
        }
        return userRepository.save(user);
    }

    public Mono<Void> deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return Mono.empty();
    }

    public boolean deleteUser(User user) {
        userRepository.delete(user);
        return true;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        return Mono.just(getUserByUsername(s));
    }

    public ConfirmUser findConfirmUserByEmail(String email) {
        return confirmUserRepository.findConfirmUserByEmail(email);
    }
    public ConfirmUser addConfirmUser(ConfirmUser user) {
        return  confirmUserRepository.save(user);
    }
}
