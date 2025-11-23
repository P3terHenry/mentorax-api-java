package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "NM_USUARIO", nullable = false, length = 100)
    private String nome;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "SENHA", nullable = false)
    private String senhaHash;

    @Column(name = "CARGO", length = 100)
    private String cargo;

    @Column(name = "TIPO_USUARIO", nullable = false, length = 20)
    private String tipoUsuario; // Exemplo: "MENTOR" ou "MENTORADO"

    @Column(name = "ATIVO", length = 1)
    private String ativo; // 'S' para sim, 'N' para não

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PerfilProfissional perfilProfissional;

}
