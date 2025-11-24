package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtualizarMentoriaResponseDTO {

    @Schema(description = "ID da mentoria atualizada", example = "1")
    private Long idMentoria;

    @Schema(description = "Novo status da mentoria", example = "CONCLUIDA")
    private String status;

    @Schema(description = "Mensagem de sucesso", example = "Mentoria atualizada com sucesso.")
    private String mensagem;

}

