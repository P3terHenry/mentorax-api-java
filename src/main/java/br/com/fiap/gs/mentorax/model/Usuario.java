package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_MENTORAX_USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "SENHA_HASH", nullable = false)
    private String senhaHash;

    @Column(name = "CARGO", length = 100)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_USUARIO", nullable = false, length = 20)
    private EnumTipoUsuario tipoUsuario; // Exemplo: "MENTOR" ou "MENTORADO"

    @Column(name = "ATIVO", length = 1)
    private String ativo; // 'S' para sim, 'N' para não

    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PerfilProfissional perfilProfissional;

    @Column(name = "CODIGO_RECUPERACAO", length = 10)
    private String codigoRecuperacao;

    @Column(name = "CODIGO_RECUPERACAO_EXPIRA_EM")
    private LocalDateTime codigoRecuperacaoExpiraEm;

    @Column(name = "CODIGO_RECUPERACAO_TENTATIVAS")
    private Integer codigoRecuperacaoTentativas = 0;

    @Column(name = "ULTIMA_RECUPERACAO_EM")
    private LocalDateTime ultimaRecuperacaoEm;

}
