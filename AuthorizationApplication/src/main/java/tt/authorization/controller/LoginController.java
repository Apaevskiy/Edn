package tt.authorization.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;
import tt.authorization.entity.ConfirmUser;
import tt.authorization.service.MailService;
import tt.authorization.service.UserService;

@Controller
@Slf4j
@AllArgsConstructor
public class LoginController {
    private final UserService userService;
    private final MailService mailService;
    @GetMapping("/")
    public Mono<Void> loginPage(HttpServerResponse response) {
        return response.sendRedirect("/login");
    }

    @PostMapping("/registration")
    public String registration(String email,
                               Model model,
                               BindingResult bindingResult) {

        if(userService.getUserByUsername(email)!=null){
            bindingResult.addError(new ObjectError("email","Данный email уже зарегистрирован!"));
            return "login";
        }
        ConfirmUser confirmUser = userService.findConfirmUserByEmail(email);
        if(confirmUser==null){
            confirmUser = userService.addConfirmUser(new ConfirmUser(email));
            mailService.sendEmail(email);
        }
        model.addAttribute("dateRegistration", confirmUser.getDateRegistration());
        return "confirmPage";
    }

    @GetMapping("/recoveryPassword")
    public String recoveryPassword() {
        return "login";
    }
}
