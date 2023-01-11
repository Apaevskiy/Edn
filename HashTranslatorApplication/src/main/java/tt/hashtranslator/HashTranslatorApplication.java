package tt.hashtranslator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class HashTranslatorApplication  {
    public static void main(String[] args) {
        SpringApplication.run(HashTranslatorApplication.class, args);
    }
}
