package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class JWTLoginRequestDTO {

    @Schema(description = "E-mail do usuário", example = "usuario@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Senha do usuário", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senha;
}
