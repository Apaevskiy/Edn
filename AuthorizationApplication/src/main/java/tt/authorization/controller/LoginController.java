package tt.authorization.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;
import tt.authorization.entity.security.ConfirmUser;
import tt.authorization.entity.security.User;
import tt.authorization.service.MailService;
import tt.authorization.service.UserService;

import java.util.Objects;

@Controller
@Slf4j
@AllArgsConstructor
public class LoginController {
    private final UserService userService;
    private final MailService mailService;

    @GetMapping("/")
    public Mono<Void> homePage(HttpServerResponse response) {
        return response.sendRedirect("/login");
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "form", required = false) String form, Model model) {
        model.addAttribute("regUser", new User());
        model.addAttribute("user", new User());
        return "/login";
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
    public String registration(@ModelAttribute User user,
                               BindingResult bindingResult,
                               Model model) {
        System.out.println("/conf: " + user);
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
//            return "redirect:/login";
        }

        if (userService.getUserByUsername(user.getUsername()) != null) {
            bindingResult.addError(new ObjectError("registrationEmail", "Данный email уже зарегистрирован!"));
            return "login";
        }

        ConfirmUser confirmUser = userService.findConfirmUserByEmail(user.getUsername());
        if (confirmUser == null) {
            confirmUser = userService.saveConfirmUser(new ConfirmUser(user.getUsername()));
            mailService.sendEmail(user.getUsername(), confirmUser.getKey());
        }
        confirmUser.setDateRegistration(confirmUser.getDateRegistration() +  5 * 60 * 1000);
        confirmUser.setKey(null);
        model.addAttribute("user", confirmUser);
        return "confirmPage";
    }

    @PostMapping("/registration")
    public String registrationPage(@ModelAttribute(name = "user") ConfirmUser confirmUser,
                                   BindingResult bindingResult,
                                   Model model) {
        System.out.println("/reg: " + confirmUser);
        ConfirmUser dbUser = userService.findConfirmUserByEmail(confirmUser.getEmail());
        if (dbUser == null) {
            return "redirect:/login";
        }
        if (!Objects.equals(confirmUser.getKey(), dbUser.getKey())){
            bindingResult.addError(new ObjectError("user", "Неверный код!"));
            model.addAttribute("user", confirmUser);
            return "confirmPage";
        }
        dbUser.setConfirm(true);
        dbUser = userService.saveConfirmUser(dbUser);
        return "changePassword";
    }


    @GetMapping("/recoveryPassword")
    public String recoveryPassword() {
        return "login";
    }
}
