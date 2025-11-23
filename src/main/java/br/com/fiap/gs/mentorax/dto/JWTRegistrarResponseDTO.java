package br.com.fiap.gs.mentorax.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTRegistrarResponseDTO {

    private String mensagem;

    private String email;

    private String tipoUsuario;
}
