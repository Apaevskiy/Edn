package tt.authorization.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import tt.authorization.service.ApiService;
import tt.authorization.service.WebClientService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ApiRestController {
    private final ApiService apiService;
    private final WebClientService webClientService;

    @PostMapping("/applications")
    public Mono<ResponseEntity<String>> createApplication(@RequestBody String jsonString) {
        List<String> hashes;
        try {
            hashes = apiService.validHashes(jsonString);
            return webClientService.createApplication()
                            .map(id -> {
                                webClientService.encryptHashesAndWriteToDb(id, hashes)
                                        .doOnError(throwable -> log.error(throwable.getMessage()))
                                        .subscribe();
                                return new ResponseEntity<>(id, HttpStatus.ACCEPTED);
                            });
        } catch (UnsupportedOperationException e) {
            return Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    @GetMapping("/applications/{id}")
    public Mono<String> getHashesOfApplication(@PathVariable String id) {
        return webClientService.getApplicationById(id).map(apiService::writeApplicationToJsonObject);
    }
}
