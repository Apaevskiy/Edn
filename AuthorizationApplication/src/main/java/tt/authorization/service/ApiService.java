package tt.authorization.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {
    public List<String> validHashes(String json) throws UnsupportedOperationException{
        try {
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            JsonArray jsonArray = jsonObject.get("hashes").getAsJsonArray();
            List<String> hashes = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                if (!isValidMD5(element.getAsString()))
                    throw new UnsupportedOperationException("Некорректные данные!");
                hashes.add(element.toString());
            }
            return hashes;
        } catch (Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }
    }
    private boolean isValidMD5(String s) {
        return s.matches("^[a-fA-F0-9]{32}$");
    }
    public String writeApplicationToJsonObject(Map<String, String> hashes){
        JsonArray jsonElements = new JsonArray();
        hashes.forEach((key, value) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(key, value);
            jsonElements.add(jsonObject);
        });
        JsonObject innerObject = new JsonObject();
        innerObject.add("hashes", jsonElements);
        return innerObject.toString();
    }
}
