package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_MENTORAX_PERFIL_PROFISSIONAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilProfissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERFIL")
    private Long idPerfil;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false, referencedColumnName = "ID_USUARIO")
    private Usuario usuario;

    @Column(name = "AREA_INTERESSE", length = 100)
    private String areaInteresse;

    @Column(name = "OBJETIVOS_PROFISSIONAIS", length = 500)
    private String objetivosProfissionais;

    @Column(name = "EXPERIENCIA_RESUMIDA", length = 500)
    private String experienciaResumida;

    @Column(name = "SOFT_SKILLS", length = 300)
    private String softSkills;

    @Column(name = "HARD_SKILLS", length = 300)
    private String hardSkills;

    @Column(name = "DISPONIBILIDADE_HORAS")
    private Integer disponibilidadeHoras;
}
