package es.ia.translator.model.dto;

import jakarta.validation.constraints.NotBlank;

public record TranslateRequest(

        @NotBlank
        String phrase

) {
}
