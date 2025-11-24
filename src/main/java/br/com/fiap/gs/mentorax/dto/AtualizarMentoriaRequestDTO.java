package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AtualizarMentoriaRequestDTO {

    @NotBlank(message = "Status é obrigatório.")
    @Schema(description = "Status da mentoria", example = "CONCLUIDA", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"ATIVA", "PAUSADA", "CONCLUIDA"})
    private String status;

    @Schema(description = "Nota de satisfação (1 a 5)", example = "4.5", minimum = "1", maximum = "5")
    private BigDecimal notaSatisfacao;

}

