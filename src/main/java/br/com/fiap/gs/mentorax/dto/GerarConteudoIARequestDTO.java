package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GerarConteudoIARequestDTO {

    @NotBlank(message = "Prompt é obrigatório.")
    @Schema(description = "Prompt/pergunta para a IA",
            example = "Como posso melhorar minhas habilidades em Spring Boot?",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String prompt;

    @Schema(description = "Tipo de geração",
            example = "RECOMENDACAO",
            allowableValues = {"RECOMENDACAO", "PLANO_ESTUDO", "ANALISE_PERFIL", "SUGESTAO_CARREIRA", "OUTRO"})
    private String tipoGeracao;

}

