package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CriarSessaoMentoriaResponseDTO {

    @Schema(description = "ID da sessão criada", example = "1")
    private Long idSessao;

    @Schema(description = "ID da mentoria", example = "1")
    private Long idMentoria;

    @Schema(description = "Assunto da sessão", example = "Planejamento de carreira")
    private String assunto;

    @Schema(description = "Data da sessão")
    private LocalDateTime dataSessao;

    @Schema(description = "Mensagem de sucesso", example = "Sessão de mentoria criada com sucesso.")
    private String mensagem;

}

