package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CriarMentoriaResponseDTO {

    @Schema(description = "ID da mentoria criada", example = "1")
    private Long idMentoria;

    @Schema(description = "ID do mentor", example = "3")
    private Long idMentor;

    @Schema(description = "Nome do mentor", example = "João Silva")
    private String nomeMentor;

    @Schema(description = "ID do mentorado", example = "5")
    private Long idMentorado;

    @Schema(description = "Nome do mentorado", example = "Maria Santos")
    private String nomeMentorado;

    @Schema(description = "Status da mentoria", example = "ATIVA")
    private String status;

    @Schema(description = "Data de início da mentoria")
    private LocalDateTime dataInicio;

    @Schema(description = "Mensagem de sucesso", example = "Mentoria criada com sucesso.")
    private String mensagem;

}

