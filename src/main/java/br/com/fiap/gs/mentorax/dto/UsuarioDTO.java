package br.com.fiap.gs.mentorax.dto;

import br.com.fiap.gs.mentorax.model.EnumTipoUsuario;
import br.com.fiap.gs.mentorax.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;
    private String nome;
    private String email;
    private EnumTipoUsuario tipoUsuario;
    private String cargo;
    private LocalDateTime dataCadastro;
    private String ativo;

    // Construtor que aceita a entidade
    public UsuarioDTO(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.tipoUsuario = usuario.getTipoUsuario();
        this.cargo = usuario.getCargo();
        this.dataCadastro = usuario.getDataCadastro();
        this.ativo = usuario.getAtivo();
    }

    // Construtor usado pela query JPQL
    public UsuarioDTO(Long idUsuario, String nome, String email,
                      EnumTipoUsuario tipoUsuario, String cargo,
                      LocalDateTime dataCadastro, String ativo) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.cargo = cargo;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
    }
}
