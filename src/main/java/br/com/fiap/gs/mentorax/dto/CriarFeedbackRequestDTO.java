package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarFeedbackRequestDTO {

    @NotNull(message = "ID da sessão é obrigatório.")
    @Schema(description = "ID da sessão de mentoria", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idSessao;

    @NotNull(message = "Nota é obrigatória.")
    @Min(value = 1, message = "Nota mínima é 1.")
    @Max(value = 5, message = "Nota máxima é 5.")
    @Schema(description = "Nota da sessão (1 a 5)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "5")
    private Integer nota;

    @Schema(description = "Comentário sobre a sessão", example = "Sessão muito produtiva, ajudou bastante!")
    private String comentario;

}

