package tt.authorization.controller;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tt.authorization.entity.security.User;
import tt.authorization.service.MailService;
import tt.authorization.service.UserService;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/users")
    public ResponseEntity<String> createNewUser(@Valid @ModelAttribute User user,
                                                BindingResult bindingResult,
                                                Model model) {
        System.out.println(user);

        if (userService.containsErrors(user, bindingResult, model)) {
            return new ResponseEntity<>(new Gson().toJson(ControllerUtils.getErrors(bindingResult)), HttpStatus.BAD_REQUEST);
        }
        String decodedPassword = user.getPassword();
        if (userService.addUser(user)) {
            try {
                user.setPassword(decodedPassword);
                mailService.sendRegistrationByAdmin(user);
                return new ResponseEntity<>(new Gson().toJson("Пользователь создан и письмо отправлено на почту"), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        new Gson().toJson( Collections.singleton("Пользователь был создан, но не удалось отправить email")),
                        HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(new Gson().toJson( Collections.singleton("Не удалось создать пользователя")), HttpStatus.BAD_REQUEST);
    }
}
