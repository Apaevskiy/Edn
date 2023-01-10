package tt.hashtranslator.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.netty.http.client.HttpClient;
import tt.hashtranslator.models.Hash;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class HttpService {
    public String parse(String hash) {
        String url = "https://www.md5cracker.com/qkhash.php?option=json&pass=";
        Hash hash1 = HttpClient.create()
                .get()
                .uri(url + hash)
                .responseSingle(((httpClientResponse, byteBufMono) -> byteBufMono.asString().map(item -> new Gson().fromJson(item, Hash.class))))
                .block();
        return hash1 != null ? hash1.getDecodedText() : "";
    }

}
