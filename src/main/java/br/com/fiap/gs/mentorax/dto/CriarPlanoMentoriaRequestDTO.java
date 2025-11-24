package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarPlanoMentoriaRequestDTO {

    @NotNull(message = "ID da mentoria é obrigatório.")
    @Schema(description = "ID da mentoria", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idMentoria;

    @Schema(description = "Se true, gera o plano com IA. Se false, usa os campos manuais.", example = "true")
    private Boolean gerarComIA = false;

    // Campos manuais (usados se gerarComIA = false)
    @Schema(description = "Cronograma manual (usado se gerarComIA=false)", example = "Semana 1: Fundamentos...")
    private String cronograma;

    @Schema(description = "Descrição geral manual", example = "Plano focado em Spring Boot")
    private String descricaoGeral;

    @Schema(description = "Metas OKR manuais", example = "O: Dominar Spring Boot - KR1: Criar 3 APIs")
    private String metasOkr;

    @Schema(description = "Mensagem motivacional manual", example = "Você consegue!")
    private String mensagemMotivacional;

}

