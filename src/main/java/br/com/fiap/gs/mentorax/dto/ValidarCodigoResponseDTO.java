package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ValidarCodigoResponseDTO {

    @Schema(description = "Indica se o código é válido", example = "true")
    private boolean codigoValido;

    @Schema(description = "Token JWT para redefinição de senha (retornado apenas se código válido)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Mensagem sobre a validação do código", example = "Código validado com sucesso.")
    private String message;
}
