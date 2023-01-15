package tt.authorization.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WebClientService {
    private final WebClient client;

    public WebClientService(@Value("${spring.web.client.url}") String clientUrl) {
        log.info("-------\n" + clientUrl + "\n-------------");
        this.client = WebClient.builder().baseUrl(clientUrl)
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
