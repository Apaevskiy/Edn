package tt.hashtranslator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    FOUND("Found"),
    NOT_FOUND("Not Found");

    private final String result;
}
