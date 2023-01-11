package tt.hashtranslator.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Hash {
    @SerializedName(value = "plaintext")
    private String decodedText;
}
