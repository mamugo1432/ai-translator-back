package es.ia.translator.controller;

import es.ia.translator.model.dto.TranslateRequest;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
public class TranslatorController {

    @PostMapping("/translate")
    public ResponseEntity<?> translatePhrase(@Valid @RequestBody TranslateRequest request, BindingResult bindingResult){
       return ResponseEntity.badRequest().build();
    }

}
