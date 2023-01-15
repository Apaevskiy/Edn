package tt.authorization.controller;

import com.google.gson.Gson;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerUtils {
    static List<String> getErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }
    static ResponseEntity<String> singleError(String message){
        return new ResponseEntity<>(new Gson().toJson(Collections.singletonList(message)), HttpStatus.BAD_REQUEST);
    }
    static ResponseEntity<String> okMessage(String message){
        return ResponseEntity.ok(new Gson().toJson(message));
    }
}
