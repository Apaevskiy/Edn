package tt.hashtranslator.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import tt.hashtranslator.models.HashesRequest;
import tt.hashtranslator.repository.HashRepository;
import tt.hashtranslator.service.HttpService;

import java.util.Map;

@RestController
@AllArgsConstructor
public class HashRestController {
    private final HttpService httpService;
    private final HashRepository repository;

//    @PostMapping("/hash")
//    Flux<String> parseHash(@RequestBody Flux<String> fluxHash) {
//        return fluxHash.map(httpService::parse);
//    }



}
