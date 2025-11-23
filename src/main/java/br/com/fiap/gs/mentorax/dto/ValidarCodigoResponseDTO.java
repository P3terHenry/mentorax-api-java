package br.com.fiap.gs.mentorax.dto;

import lombok.Data;

@Data
public class ValidarCodigoResponseDTO {

    private boolean codigoValido;
    private String token;
    private String message;
}
