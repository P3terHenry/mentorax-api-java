package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ValidarCodigoRequestDTO {

    private String email;

    private String codigo;
}
