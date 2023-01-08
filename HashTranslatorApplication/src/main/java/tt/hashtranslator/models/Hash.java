package tt.hashtranslator.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Hash {
    @SerializedName(value = "md5hash")
    private String md5;
    @SerializedName(value = "plaintext")
    private String decodedText;
    private String status;
}
