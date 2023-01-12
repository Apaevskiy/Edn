package tt.authorization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class AppController {
    @GetMapping("/")
    public String mainPage( Model model) {
        return "api/appPage";
    }
    @GetMapping("/gln")
    public String glnPage(){
        return "api/glnPage";
    }

    @GetMapping("/confirm")
    public String mainPag() {
        return "api/confirm";
    }
}
