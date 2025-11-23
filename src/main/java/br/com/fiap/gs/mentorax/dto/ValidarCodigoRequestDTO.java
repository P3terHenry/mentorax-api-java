package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ValidarCodigoRequestDTO {

    @Schema(description = "E-mail do usuário", example = "usuario@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Código de verificação recebido por e-mail", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigo;
}
