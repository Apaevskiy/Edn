package tt.authorization.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class WebClientService {
    private final WebClient client;


    public WebClientService() {
        this.client = WebClient.builder().baseUrl("http://localhost:9091")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    public Mono<String> createApplication() {
        return client.get().uri("/createHashes")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<Map> getApplicationById(String id) {
        return client.get().uri("/getHashes/" + id)
                .retrieve()
                .bodyToMono(Map.class);
    }

    public Mono<Void> encryptHashesAndWriteToDb(String id, List<String> hashes) {
        return client.post().uri("/encryptHashes/" + id)
                .bodyValue(hashes)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
