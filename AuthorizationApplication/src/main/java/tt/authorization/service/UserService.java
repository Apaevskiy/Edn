package tt.authorization.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import tt.authorization.entity.Role;
import tt.authorization.entity.User;
import tt.authorization.repository.RoleRepository;
import tt.authorization.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = bCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) {
        return getUserByUsername(username);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFormDb = userRepository.findByUsername(user.getUsername());

        if (userFormDb != null) {
            return false;
        }
        user.setActive(true);
        user.setDateRegistration(new Date().getTime());

        if (user.getRole() == null) {
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
        return userRepository.findByActivationCode(key);
    }

    public User activateUser(String key) {
        User user = userRepository.findByActivationCode(key);
        if (user == null) {
            return null;
        }
        user.setActivationCode(null);

        return userRepository.save(user);
    }

    public boolean containsErrors(User user, BindingResult bindingResult) {
        if (user.getPassword()!=null && !user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.addError(new ObjectError("passwordConfirm", "Пароли не совпадают"));
        }
        return bindingResult.hasErrors();
    }

    public User updatePassword(User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser==null ||
                !dbUser.getUsername().equals(user.getUsername()) ||
                !dbUser.getActivationCode().equals(user.getActivationCode()))
            return null;

        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        dbUser.setActivationCode(null);

        return userRepository.save(dbUser);
    }

    public User updateActivationCode(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return null;
        user.setActivationCode(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public Set<User> findAllAndFilter(String email, Long roleId) {
        if (email != null && email.isEmpty())
            email = null;
        return userRepository.findAllAndFilterByEmailAndRole(email, roleId);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public String updateUser(User user) {
        User dbUser = userRepository.findFirstById(user.getId());
        if(dbUser==null )
            return "Пользователь не найден";
        if (!user.getUsername().equals(dbUser.getUsername()) && userRepository.existsByUsername(user.getUsername())){
            return "Email уже зарегистрирован";
        }
        if(user.getPassword()!=null && passwordEncoder.matches(user.getPassword(), dbUser.getPassword()))
            dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        dbUser.setRole(user.getRole());
        dbUser.setActive(user.isActive());
        dbUser.setUsername(user.getUsername());
        userRepository.save(dbUser);
        return null;
    }

    @PostConstruct
    public void createDefaultUser(){
        Role adminRole = roleRepository.findFirstByName("ROLE_ADMIN");
        if(adminRole==null){
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setFullName("Администратор");
            roleRepository.save(adminRole);
        }
        User user = userRepository.findByUsername("a@edn.by");
        if(user==null){
            user = new User();
            user.setUsername("a@edn.by");
            user.setActive(true);
            user.setPassword(passwordEncoder.encode("123"));
            user.setRole(adminRole);
            userRepository.save(user);
        }
    }
}
