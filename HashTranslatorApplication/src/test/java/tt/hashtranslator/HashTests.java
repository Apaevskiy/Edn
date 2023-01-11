package tt.hashtranslator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import tt.hashtranslator.controller.HashRestController;
import tt.hashtranslator.repository.AppRepository;
import tt.hashtranslator.service.HttpService;

import java.util.Collections;

@RunWith(SpringRunner.class)
@WebFluxTest(HashRestController.class)
public class HashTests {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    private HttpService httpService;
    @MockBean
    private AppRepository appRepository;

    @Test
    public void testParseHash() {
        this.webClient.post()
                .uri("http://localhost:9090/hash")
                .body(Flux.just("0800fc577294c34e0b28ad2839435945"), String.class)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class)
                .isEqualTo(Collections.singletonList("hash"));
    }
}
