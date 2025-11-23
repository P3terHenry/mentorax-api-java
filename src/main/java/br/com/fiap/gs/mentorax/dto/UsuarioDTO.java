package br.com.fiap.gs.mentorax.dto;

import br.com.fiap.gs.mentorax.model.EnumTipoUsuario;
import br.com.fiap.gs.mentorax.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UsuarioDTO {

    @Schema(description = "ID do usuário", example = "1")
    private Long idUsuario;

    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@exemplo.com")
    private String email;

    @Schema(description = "Tipo de usuário no sistema", example = "MENTORADO")
    private EnumTipoUsuario tipoUsuario;

    @Schema(description = "Cargo do usuário", example = "Desenvolvedor")
    private String cargo;

    @Schema(description = "Data de cadastro do usuário")
    private LocalDateTime dataCadastro;

    @Schema(description = "Status de ativação do usuário", example = "S")
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
