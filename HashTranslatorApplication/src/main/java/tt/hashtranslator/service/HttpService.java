package tt.hashtranslator.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class HttpService {
    public String firstDecrypt(String hash) { // 3000+ ms (3 hash)
        return new Gson().fromJson(
                        decrypt("https://www.md5cracker.com/qkhash.php?option=json&pass="+ hash, null),
                        JsonObject.class)
                .get("plaintext").getAsString();
    }
    public String secondDecrypt(String hash) { // 400 - 1000 ms (3 hash)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
        String body = decrypt("https://api.allinone.tools/encryption/decrypt/md5/" + hash, headers);
        return body.equals("Password not found !")? "" : body;
    }
    private String decrypt(String url, HttpHeaders headers){
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody();
    }
}
