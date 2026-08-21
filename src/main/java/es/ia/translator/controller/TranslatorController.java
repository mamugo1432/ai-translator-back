package es.ia.translator.controller;

import es.ia.translator.exceptions.translate.TranslateException;
import es.ia.translator.exceptions.user.UserException;
import es.ia.translator.model.dto.TranslateRequest;
import es.ia.translator.service.IAService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Data
public class TranslatorController {

    private final IAService iaService;

    @PostMapping("/translate")
    public ResponseEntity<?> translatePhrase(@Valid @RequestBody TranslateRequest request, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map( error -> error.getField() + ":" + error.getDefaultMessage())
                    .toList();
            throw new TranslateException(errors);
        }

        String response = this.iaService.tranlatePhrase(request);

        return ResponseEntity.ok(Map.of("response",response));
    }

}
