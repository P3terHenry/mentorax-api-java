package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RecuperarSenhaRequestDTO {

    @Schema(description = "Nova senha do usuário", example = "novaSenha123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String novaSenha;

    @Schema(description = "Confirmação da nova senha", example = "novaSenha123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmacaoNovaSenha;
}
