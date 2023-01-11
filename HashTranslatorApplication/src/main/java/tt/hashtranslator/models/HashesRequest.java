package tt.hashtranslator.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("applications")
public class HashesRequest {
    @Id
    private long id;
    private List<String> hashes;
}
