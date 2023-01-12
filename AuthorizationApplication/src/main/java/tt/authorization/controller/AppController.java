package tt.authorization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping("/app")
    public String mainPage( Model model) {
        return "app/appPage";
    }

    @GetMapping("/confirm")
    public String mainPag() {
        return "app/confirm";
    }
}
