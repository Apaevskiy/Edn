package tt.authorization.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tt.authorization.entity.security.Role;
import tt.authorization.entity.security.User;
import tt.authorization.repository.secutiry.RoleRepository;
import tt.authorization.repository.secutiry.UserRepository;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService, ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        return Mono.just(getUserByUsername(s));
    }

    public boolean addUser(User user) {
        User userFormDb = userRepository.findByUsername(user.getUsername());

        if (userFormDb != null) {
            return false;
        }
        user.setActive(true);
        user.setDateRegistration(new Date().getTime());
        Optional<Role> role = roleRepository.findById(1L);
        role.ifPresent(value -> user.setRoles(Collections.singleton(value)));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        return true;
    }
}
