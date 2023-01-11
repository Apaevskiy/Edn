package tt.hashtranslator.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tt.hashtranslator.models.Application;
import tt.hashtranslator.models.Request;
import tt.hashtranslator.repository.AppRepository;
import tt.hashtranslator.service.HttpService;

@RestController
@AllArgsConstructor
public class HashRestController {
    private final HttpService httpService;
    private final AppRepository repository;
    @PostMapping("/createHashes")
    public Mono<String> generateId() {
        return repository.save(new Application()).map(Application::getId);
    }

    @PostMapping("/encryptHashes/{id}")
    public Mono<Application> encryptHashesAndWriteToDb(@PathVariable("id") String id, @RequestBody Request request) {
        return repository.findById(id).flatMap(app -> {
            request.getHashes().forEach(s -> app.getHash().put(s, httpService.secondDecrypt(s)));
            return repository.save(app);
        });
    }
}
