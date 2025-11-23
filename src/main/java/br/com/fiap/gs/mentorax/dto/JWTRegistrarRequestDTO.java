package br.com.fiap.gs.mentorax.dto;

import br.com.fiap.gs.mentorax.model.EnumTipoUsuario;
import lombok.Data;

@Data
public class JWTRegistrarRequestDTO {

    private String email;
    private String nome;
    private String cargo;
    private EnumTipoUsuario tipoUsuario;
    private String senha;
}
