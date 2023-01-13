package tt.authorization.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tt.authorization.entity.security.User;
import tt.authorization.service.MailService;
import tt.authorization.service.UserService;

@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class ApiController {
    private final UserService userService;
    private final MailService mailService;

    @GetMapping("/")
    public String mainPage(Model model) {
        return "api/appPage";
    }

    @GetMapping("/gln")
    public String glnPage() {
        return "api/glnPage";
    }

    @GetMapping("/confirm")
    public String mainPage() {
        return "api/confirm";
    }

    @GetMapping("/users")
    public String usersPage(@RequestParam(name = "email", required = false) String email,
                            @RequestParam(name = "roleId", required = false) Long roleId,
                            Model model) {
        System.out.println(email);
        System.out.println(roleId);
        System.out.println(userService.findAllAndFilter(email, roleId));
        model.addAttribute("users", userService.findAllAndFilter(email, roleId));
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getRoles());
        return "api/usersPage";
    }

}
