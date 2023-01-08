package tt.hashtranslator.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import tt.hashtranslator.service.HttpService;

@RestController
@AllArgsConstructor
public class HashRestController {
    private final HttpService httpService;

    @PostMapping("/hash")
    Flux<String> parseHash(@RequestBody Flux<String> fluxHash) {
        return fluxHash.map(httpService::parse);
    }
}
