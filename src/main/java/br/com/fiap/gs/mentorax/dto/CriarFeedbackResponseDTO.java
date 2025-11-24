package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CriarFeedbackResponseDTO {

    @Schema(description = "ID do feedback criado", example = "1")
    private Long idFeedback;

    @Schema(description = "ID da sessão", example = "1")
    private Long idSessao;

    @Schema(description = "Nota atribuída", example = "5")
    private Integer nota;

    @Schema(description = "Mensagem de sucesso", example = "Feedback enviado com sucesso.")
    private String mensagem;

}

