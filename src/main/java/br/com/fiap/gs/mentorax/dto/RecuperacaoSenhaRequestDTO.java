package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RecuperacaoSenhaRequestDTO {

    @Schema(description = "E-mail do usuário para recuperação de senha", example = "usuario@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
