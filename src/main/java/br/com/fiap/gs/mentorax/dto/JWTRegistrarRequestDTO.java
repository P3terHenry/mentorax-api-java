package br.com.fiap.gs.mentorax.dto;

import lombok.Data;

@Data
public class JWTRegistrarRequestDTO {

    private String email;

    private String nome;

    private String cargo;

    private String tipoUsuario;

    private String senha;
}
