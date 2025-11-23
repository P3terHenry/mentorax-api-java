package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class JWTLoginResponseDTO {

    @Schema(description = "Token JWT gerado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "ID do usuário (subject do token)", example = "1")
    private String subject;

    @Schema(description = "Data de emissão do token")
    private Date IssuedAt;

    @Schema(description = "Data de expiração do token")
    private Date expiration;
}
