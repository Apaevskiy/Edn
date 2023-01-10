package tt.authorization.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerUtils {
    static List<String> getErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }
}
