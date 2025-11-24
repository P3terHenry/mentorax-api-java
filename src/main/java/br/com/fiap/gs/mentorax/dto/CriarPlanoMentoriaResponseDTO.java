package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CriarPlanoMentoriaResponseDTO {

    @Schema(description = "ID do plano criado", example = "1")
    private Long idPlano;

    @Schema(description = "ID da mentoria", example = "1")
    private Long idMentoria;

    @Schema(description = "Data de geração do plano")
    private LocalDateTime dataGeracao;

    @Schema(description = "Se foi gerado com IA", example = "true")
    private Boolean geradoComIA;

    @Schema(description = "Mensagem de sucesso", example = "Plano de mentoria criado com sucesso.")
    private String mensagem;

}

