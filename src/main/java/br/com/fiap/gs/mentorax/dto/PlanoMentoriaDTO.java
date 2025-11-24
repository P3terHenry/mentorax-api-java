package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PlanoMentoriaDTO {

    @Schema(description = "ID do plano", example = "1")
    private Long idPlano;

    @Schema(description = "ID da mentoria", example = "1")
    private Long idMentoria;

    @Schema(description = "Cronograma")
    private String cronograma;

    @Schema(description = "Descrição geral")
    private String descricaoGeral;

    @Schema(description = "Metas OKR")
    private String metasOkr;

    @Schema(description = "Mensagem motivacional")
    private String mensagemMotivacional;

    @Schema(description = "Data de geração")
    private LocalDateTime dataGeracao;

}

