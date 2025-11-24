package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CriarSessaoMentoriaRequestDTO {

    @NotNull(message = "ID da mentoria é obrigatório.")
    @Schema(description = "ID da mentoria", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idMentoria;

    @NotBlank(message = "Assunto é obrigatório.")
    @Schema(description = "Assunto da sessão", example = "Planejamento de carreira", requiredMode = Schema.RequiredMode.REQUIRED)
    private String assunto;

    @NotBlank(message = "Resumo da sessão é obrigatório.")
    @Schema(description = "Resumo da sessão de mentoria", example = "Discutimos os objetivos de carreira para os próximos 6 meses", requiredMode = Schema.RequiredMode.REQUIRED)
    private String resumoSessao;

    @Schema(description = "Próximos passos acordados", example = "Estudar Spring Boot e criar projeto portfolio")
    private String proximosPassos;

    @Schema(description = "Humor detectado do mentorado", example = "Motivado")
    private String humorDetectado;

}
