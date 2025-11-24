package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GerarConteudoIAResponseDTO {

    @Schema(description = "ID do histórico gerado", example = "1")
    private Long idHistorico;

    @Schema(description = "Prompt enviado", example = "Como posso melhorar minhas habilidades em Spring Boot?")
    private String promptEnviado;

    @Schema(description = "Resposta da IA")
    private String respostaIA;

    @Schema(description = "Tipo de geração", example = "RECOMENDACAO")
    private String tipoGeracao;

    @Schema(description = "Data de execução")
    private LocalDateTime dataExecucao;

}

