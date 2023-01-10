package tt.authorization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping("/app")
    public String mainPage() {
        return "app/confirm";
    }

    @GetMapping("/confirm")
    public String mainPag() {
        return "app/confirm";
    }
}
