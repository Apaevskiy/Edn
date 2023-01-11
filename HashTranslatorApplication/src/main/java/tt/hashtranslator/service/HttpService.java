package tt.hashtranslator.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import tt.hashtranslator.models.Hash;

@Service
@Slf4j
public class HttpService {
    public Mono<Hash> parse(String hash) {
        String url = "https://www.md5cracker.com/qkhash.php?option=json&pass=";
        return HttpClient.create()
                .get()
                .uri(url + hash)
                .responseSingle((httpClientResponse, byteBufMono) -> byteBufMono.asString().map(item -> new Gson().fromJson(item, Hash.class)));
    }

}
