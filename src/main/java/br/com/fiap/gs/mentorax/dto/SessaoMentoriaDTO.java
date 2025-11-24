package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SessaoMentoriaDTO {

    @Schema(description = "ID da sessão", example = "1")
    private Long idSessao;

    @Schema(description = "ID da mentoria", example = "1")
    private Long idMentoria;

    @Schema(description = "Data da sessão")
    private LocalDateTime dataSessao;

    @Schema(description = "Assunto da sessão", example = "Planejamento de carreira")
    private String assunto;

    @Schema(description = "Resumo da sessão", example = "Discutimos os objetivos de carreira")
    private String resumoSessao;

    @Schema(description = "Próximos passos", example = "Estudar Spring Boot")
    private String proximosPassos;

    @Schema(description = "Humor detectado", example = "Motivado")
    private String humorDetectado;

}

