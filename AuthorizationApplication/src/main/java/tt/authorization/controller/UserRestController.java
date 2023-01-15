package tt.authorization.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tt.authorization.entity.User;
import tt.authorization.service.MailService;
import tt.authorization.service.UserService;

import javax.validation.Valid;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final MailService mailService;

    @PutMapping
    public ResponseEntity<String> createNewUser(@Valid @ModelAttribute User user,
                                                BindingResult bindingResult) {

        if (userService.containsErrors(user, bindingResult)) {
            return new ResponseEntity<>(new Gson().toJson(ControllerUtils.getErrors(bindingResult)), HttpStatus.BAD_REQUEST);
        }
        String decodedPassword = user.getPassword();
        if (userService.addUser(user)) {
            try {
                mailService.sendRegistrationByAdmin(user, decodedPassword);
                return ControllerUtils.okMessage("Пользователь создан и письмо отправлено на почту");
            } catch (Exception e) {
                return ControllerUtils.singleError("Пользователь был создан, но не удалось отправить email");
            }
        }

        return ControllerUtils.singleError("Не удалось создать пользователя");
    }

    @GetMapping("/{username}")
    public ResponseEntity<String> createNewUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user == null)
            return ControllerUtils.singleError("Пользователь не найден");
        JsonObject gson = new Gson().toJsonTree(user).getAsJsonObject();
        gson.remove("password");
        return ResponseEntity.ok(gson.toString());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(User user) {
        try {
            // Тут должна быть проверка, в одной ли организации сотрудники
            userService.deleteUser(user);
            return ControllerUtils.okMessage("Пользователь успешно удалён");
        } catch (Exception e) {
            return ControllerUtils.singleError("Ошибка удаления пользователя");
        }
    }

    @PatchMapping
    public ResponseEntity<String> updateUser(User user) {
        if (!Pattern.compile(".+[@].+[\\.].+").matcher(user.getUsername()).find()) {
            return ControllerUtils.singleError("Не корректный email");
        }
        if (user.getPassword() != null && !user.getPassword().equals(user.getConfirmPassword()))
            return ControllerUtils.singleError("Пароли не совпадают");
        String saveMessage = userService.updateUser(user);
        if (saveMessage!=null) {
            return ControllerUtils.singleError(saveMessage);
        }
        return ControllerUtils.okMessage("Пользователь успешно обновлён");
    }
}
