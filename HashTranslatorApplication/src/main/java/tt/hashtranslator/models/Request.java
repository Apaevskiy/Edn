package tt.hashtranslator.models;

import lombok.Data;

import java.util.List;

@Data
public class Request {
    private List<String> hashes;
}
