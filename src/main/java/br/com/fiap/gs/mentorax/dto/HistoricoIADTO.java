package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HistoricoIADTO {

    @Schema(description = "ID do histórico", example = "1")
    private Long idHistorico;

    @Schema(description = "ID do usuário", example = "5")
    private Long idUsuario;

    @Schema(description = "Nome do usuário", example = "João Silva")
    private String nomeUsuario;

    @Schema(description = "Prompt enviado", example = "Como melhorar em Spring Boot?")
    private String promptEnviado;

    @Schema(description = "Resposta da IA (resumida)")
    private String respostaIA;

    @Schema(description = "Tipo de geração", example = "RECOMENDACAO")
    private String tipoGeracao;

    @Schema(description = "Data de execução")
    private LocalDateTime dataExecucao;

}

