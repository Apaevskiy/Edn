package tt.hashtranslator.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Document("applications")
public class Application {
    @Id
    private String id;
    private Map<String, String> hash = new HashMap<>();

    @Override
    public String toString() {
        return hash.toString();
    }
}
