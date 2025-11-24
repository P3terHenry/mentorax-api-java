package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackDTO {

    @Schema(description = "ID do feedback", example = "1")
    private Long idFeedback;

    @Schema(description = "ID da sessão", example = "1")
    private Long idSessao;

    @Schema(description = "Assunto da sessão", example = "Planejamento de carreira")
    private String assuntoSessao;

    @Schema(description = "Nota (1 a 5)", example = "5")
    private Integer nota;

    @Schema(description = "Comentário", example = "Sessão muito produtiva!")
    private String comentario;

}

