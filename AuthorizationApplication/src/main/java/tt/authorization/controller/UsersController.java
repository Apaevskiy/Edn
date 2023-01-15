package tt.authorization.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tt.authorization.entity.User;
import tt.authorization.service.UserService;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {
    private final UserService userService;

    @GetMapping
    public String usersPage(@RequestParam(name = "email", required = false) String email,
                            @RequestParam(name = "roleId", required = false) Long roleId,
                            Model model) {
        model.addAttribute("users", userService.findAllAndFilter(email, roleId));
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getRoles());
        return "api/usersPage";
    }

}
