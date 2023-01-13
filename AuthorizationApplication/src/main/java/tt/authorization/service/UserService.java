package tt.authorization.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import tt.authorization.entity.security.Role;
import tt.authorization.entity.security.User;
import tt.authorization.repository.secutiry.RoleRepository;
import tt.authorization.repository.secutiry.UserRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = bCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFormDb = userRepository.findByUsername(user.getUsername());

        if (userFormDb != null) {
            return false;
        }
        user.setActive(true);
        user.setDateRegistration(new Date().getTime());

        if(user.getRole()!=null){
            Optional<Role> role = roleRepository.findById(1L);
            role.ifPresent(user::setRole);
            user.setActivationCode(UUID.randomUUID().toString());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User recoveryPassword(String key) {
        User user = userRepository.findByActivationCode(key);
        if(user==null){
            return null;
        }
        return user;
    }
    public User activateUser(String key) {
        User user = userRepository.findByActivationCode(key);
        if(user==null){
            return null;
        }
        user.setActivationCode(null);

        return userRepository.save(user);
    }
    public boolean containsErrors(User user, BindingResult bindingResult, Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.addError(new ObjectError("passwordConfirm", "Пароли не совпадают"));
        }
        return bindingResult.hasErrors();
    }

    public User updatePassword(User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());
        if(!dbUser.getUsername().equals(user.getUsername())
        || !dbUser.getActivationCode().equals(user.getActivationCode()))
            return null;

        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        dbUser.setActivationCode(null);

        return userRepository.save(dbUser);
    }

    public User updateActivationCode(String username) {
        User user = userRepository.findByUsername(username);
        if(user==null)
            return null;
        user.setActivationCode(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public Set<User> findAllAndFilter(String email, Long roleId){
        if(email!= null && email.isEmpty())
            email = null;
        return userRepository.findAllAndFilterByEmailAndRole(email, roleId);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
