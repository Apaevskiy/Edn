package tt.authorization.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tt.authorization.entity.security.User;
import tt.authorization.service.MailService;
import tt.authorization.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@Slf4j
public class LoginController {
    @Value("${server.port}")
    private String serverPort;
    private final UserService userService;
    private final MailService mailService;

    public LoginController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping(path = {"/","/login","/recoveryPassword","/registration"})
    public String loginPage(@RequestParam(name = "form", required = false) String form, Model model) {
        model.addAttribute("regUser", new User());
        model.addAttribute("user", new User());
        return "login/loginPage";
    }

    @PostMapping("/login")
    public Mono<String> login(ServerWebExchange exchange) {

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(User.class)
//                .doOnNext(userDetails -> {
//                    addTokenHeader(exchange.getResponse(), userDetails); // your job to code it the way you want
//                })
                .map(u -> "redirect:/app");
    }

    @PostMapping("/confirmEmail")
    public String confirmEmail(@ModelAttribute User user,
                               BindingResult bindingResult,
                               Model model) {
        System.out.println("/conf: " + user);

        User dbUser = userService.getUserByUsername(user.getUsername());
        if (dbUser == null) {
            return "redirect:/login";
        }

        dbUser.setDateRegistration(dbUser.getDateRegistration() + 5 * 60 * 1000);
        dbUser.setActivationCode(UUID.randomUUID().toString());
        model.addAttribute("user", dbUser);
        try {
            mailService.sendEmail(user.getUsername(), user.getActivationCode());
        } catch (MessagingException ignored) {
        }
        return "login/confirmPage";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute User user,
                               BindingResult bindingResult,
                               Model model) {
        System.out.println("/reg: " + user);

       /* if(!user.getPassword().equals(confirmPassword)){
            bindingResult.addError(new ObjectError("regUser", "Пароли не совпадают"));
            return "login/loginPage";
        }*/

        model.addAttribute("user", new User());
        model.addAttribute("regUser", user);
        if (bindingResult.hasErrors()) {
            System.out.println("error: ");
            model.addAttribute("regError", ControllerUtils.getErrors(bindingResult));
            return "login/loginPage";
        }
        return "login/confirmPage";
        /*if (!userService.addUser(user)) {
            bindingResult.addError(new ObjectError("regUser", "Данный email уже зарегистрирован"));
            return "login/confirmPage";
        }
        try {
            mailService.sendEmail(user.getUsername(), user.getActivationCode());
            model.addAttribute("email", user.getUsername());
            return "login/confirmPage";
        } catch (MessagingException e) {
            bindingResult.addError(new ObjectError("regUser", "Ошибка отправки письма на данную почту"));
            return "login/loginPage";
        }*/

    }


    @PostMapping("/recoveryPassword")
    public String recoveryPassword() {
        return "login/loginPage";
    }
}
