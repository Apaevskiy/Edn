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
import tt.authorization.entity.security.ConfirmUser;
import tt.authorization.entity.security.User;
import tt.authorization.repository.secutiry.ConfirmUserRepository;
import tt.authorization.repository.secutiry.RoleRepository;
import tt.authorization.repository.secutiry.UserRepository;

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

    public ConfirmUser saveConfirmUser(ConfirmUser user) {
        return confirmUserRepository.save(user);
    }
}
