package tt.authorization.config;

import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {
    private final WebClient client;

    public WebClientConfig() {
        this.client = WebClient.builder().baseUrl("http://localhost:9090")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

}
