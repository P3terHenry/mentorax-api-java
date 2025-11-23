package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTValidarTokenResponseDTO {

    @Schema(description = "Token JWT validado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Status de validação do token", example = "true")
    private boolean statusToken;

    @Schema(description = "Mensagem sobre a validação", example = "Token válido.")
    private String message;
}
