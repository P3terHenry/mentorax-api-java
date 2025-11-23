package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecuperacaoResponseDTO {

    @Schema(description = "Mensagem de resposta", example = "E-mail de recuperação encaminhado.")
    private String message;
}
