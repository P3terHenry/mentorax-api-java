package br.com.fiap.gs.mentorax.dto;

import br.com.fiap.gs.mentorax.model.EnumTipoUsuario;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    private String nome;
    private String email;
    private EnumTipoUsuario tipoUsuario;
    private String cargo;
    private String ativo;


}
