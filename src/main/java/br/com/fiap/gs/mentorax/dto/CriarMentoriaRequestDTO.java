package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarMentoriaRequestDTO {

    @NotNull(message = "ID do mentorado é obrigatório.")
    @Schema(description = "ID do usuário mentorado", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idMentorado;

}

