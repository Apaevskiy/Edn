package tt.hashtranslator.controller;

//import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import tt.hashtranslator.models.Application;
import tt.hashtranslator.repository.AppRepository;
import tt.hashtranslator.service.HttpService;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class HashRestController {
    private final HttpService httpService;
    private final AppRepository repository;
    @GetMapping("/createHashes")
    public Mono<String> generateId() {
        return repository.save(new Application()).map(Application::getId);
    }

    @PostMapping("/encryptHashes/{id}")
    public Mono<Map<String, String>> encryptHashesAndWriteToDb(@PathVariable("id") String id, @RequestBody List<String> hashes) {
        return repository.findById(id).flatMap(app -> {
            hashes.forEach(s -> {
                s = s.replaceAll("\"", "");
                app.getHash().put(s, httpService.secondDecrypt(s));
            });
            return repository.save(app);
        }).map(Application::getHash);
    }
    @GetMapping("/getHashes/{id}")
    public Mono<Map<String, String>> encryptHashesAndWriteToDb(@PathVariable("id") String id) {
        return repository.findById(id).map(Application::getHash);
    }



}
