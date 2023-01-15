package tt.authorization.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import tt.authorization.entity.User;
import tt.authorization.service.MailService;
import tt.authorization.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.regex.Pattern;

@Controller
@Slf4j
public class LoginController {

    private final UserService userService;
    private final MailService mailService;

    public LoginController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping("/")
    public String mainPage(Authentication authentication) {
        return authentication!=null && authentication.isAuthenticated() ?
                "redirect:/api" :
                "redirect:/login";
    }

    @GetMapping(path = {"/login", "/registration"})
    public String loginPage(@RequestParam(name = "form", required = false) String form, Model model) {
        model.addAttribute("user", new User());
        return "login/loginPage";
    }


    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute User user,
                               BindingResult bindingResult,
                               Model model) {

        if (userService.containsErrors(user, bindingResult)) {
            model.addAttribute("user", user);
            model.addAttribute("regError", ControllerUtils.getErrors(bindingResult));
            return "login/loginPage";
        }

        if (!userService.addUser(user)) {
            bindingResult.addError(new ObjectError("userExistError", "Данный email уже зарегистрирован"));
            model.addAttribute("user", user);
            model.addAttribute("regError", ControllerUtils.getErrors(bindingResult));
            return "login/loginPage";
        }
        try {
            mailService.sendRegistrationEmail(user.getUsername(), user.getActivationCode());
            user.setDateRegistration(user.getDateRegistration() + 300000);
            model.addAttribute("user", user);
            return "login/confirmPage";
        } catch (Exception e) {
            log.error(e.getMessage());
            bindingResult.addError(new ObjectError("regUser", "Ошибка отправки письма на данную почту"));
            model.addAttribute("regError", ControllerUtils.getErrors(bindingResult));
            return "login/loginPage";
        }
    }

    @PostMapping("/recovery")
    public String recovery(@ModelAttribute User user,
                           Model model) {
        if (!Pattern.compile(".+[@].+[\\.].+").matcher(user.getUsername()).find()) {
            model.addAttribute("user", user);
            model.addAttribute("recoveryError", Collections.singleton("Неверный формат почты"));
            return "login/loginPage";
        }

        User dbUser = userService.updateActivationCode(user.getUsername());
        if (dbUser == null) {
            model.addAttribute("user", user);
            model.addAttribute("recoveryError", Collections.singleton("Email адрес не найден"));
            return "login/loginPage";
        }
        try {
            dbUser.setDateRegistration(dbUser.getDateRegistration() + 300000);
            mailService.sendRecoveryEmail(dbUser.getUsername(), dbUser.getActivationCode());
            model.addAttribute("user", dbUser);
            return "login/confirmPage";
        } catch (Exception e) {
            model.addAttribute("regError", Collections.singleton("Ошибка отправки письма на данную почту"));
            return "login/loginPage";
        }
    }

    @GetMapping("/activate/{key}")
    public String confirmEmail(@PathVariable(name = "key") String key, Model model) {
        User activatedUser = userService.activateUser(key);

        if (activatedUser != null) {
            model.addAttribute("user", activatedUser);
            model.addAttribute("regSuccess", "Регистрация прошла успешна");
        } else {
            model.addAttribute("regError", Collections.singleton("Код активации не найден"));
            model.addAttribute("user", new User());
        }
        return "login/loginPage";
    }

    @GetMapping("/recovery/{key}")
    public String recoveryPasswordPage(@PathVariable(name = "key") String key, Model model) {
        User activatedUser = userService.recoveryPassword(key);

        if (activatedUser != null) {
            activatedUser.setPassword(null);
            model.addAttribute("user", activatedUser);
            model.addAttribute("uuid", key);
            return "login/recoveryPasswordPage";
        } else {
            model.addAttribute("recoveryError", Collections.singleton("Email не найден"));
            model.addAttribute("user", new User());
        }
        return "login/loginPage";
    }

    @PostMapping("/recoveryPassword")
    public String recoveryPassword(@Valid @ModelAttribute User user,
                                   BindingResult bindingResult,
                                   Model model) {
        if (userService.containsErrors(user, bindingResult)) {
            model.addAttribute("user", user);
            model.addAttribute("uuid", user.getActivationCode());
            model.addAttribute("recoveryError", ControllerUtils.getErrors(bindingResult));
            return "login/recoveryPasswordPage";
        }
        User dbUser = userService.updatePassword(user);
        if (dbUser == null)
            return "redirect:/login";

        model.addAttribute("user", dbUser);
        model.addAttribute("regSuccess", "Пароль успешно восстановлен");
        return "login/loginPage";
    }

}
