package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MentoriaDTO {

    @Schema(description = "ID da mentoria", example = "1")
    private Long idMentoria;

    @Schema(description = "ID do mentor", example = "3")
    private Long idMentor;

    @Schema(description = "Nome do mentor", example = "João Silva")
    private String nomeMentor;

    @Schema(description = "Email do mentor", example = "joao.mentor@exemplo.com")
    private String emailMentor;

    @Schema(description = "ID do mentorado", example = "5")
    private Long idMentorado;

    @Schema(description = "Nome do mentorado", example = "Maria Santos")
    private String nomeMentorado;

    @Schema(description = "Email do mentorado", example = "maria.mentorada@exemplo.com")
    private String emailMentorado;

    @Schema(description = "Data de início da mentoria")
    private LocalDateTime dataInicio;

    @Schema(description = "Data de fim da mentoria")
    private LocalDateTime dataFim;

    @Schema(description = "Status da mentoria", example = "ATIVA")
    private String status;

    @Schema(description = "Nota de satisfação", example = "4.5")
    private BigDecimal notaSatisfacao;

}

