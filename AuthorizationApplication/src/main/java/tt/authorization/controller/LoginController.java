package tt.authorization.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;
import tt.authorization.entity.ConfirmUser;
import tt.authorization.entity.User;
import tt.authorization.service.MailService;
import tt.authorization.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Date;

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
        return "/login";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user,
                               BindingResult bindingResult,
                               Model model) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            bindingResult.addError(new ObjectError("registrationEmail", "Данный email уже зарегистрирован!"));
            return "login";
        }

        ConfirmUser confirmUser = userService.findConfirmUserByEmail(user.getUsername());  // check timer
        if (confirmUser == null) {
            confirmUser = userService.addConfirmUser(new ConfirmUser(user.getUsername()));
            mailService.sendEmail(user.getUsername());
        }
        model.addAttribute("date", confirmUser.getDateRegistration()+5*60*1000);
        System.out.println(confirmUser.getDateRegistration()+5*60*1000);
        model.addAttribute("email", user.getUsername());
        return "confirmPage";
    }
    @PostConstruct
    public void test(){
        System.out.println("now: " + new Date().getTime());
    }
    @GetMapping("/recoveryPassword")
    public String recoveryPassword() {
        return "login";
    }
}
