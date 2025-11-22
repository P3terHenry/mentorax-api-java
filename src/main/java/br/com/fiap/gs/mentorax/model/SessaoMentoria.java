package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_MENTORAX_SESSAO_MENTORIA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessaoMentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SESSAO")
    private Long idSessao;

    @ManyToOne
    @JoinColumn(name = "ID_MENTORIA", referencedColumnName = "ID_MENTORIA")
    private Mentoria mentoria;

    @Column(name = "DATA_SESSAO")
    private LocalDateTime dataSessao;

    @Column(length = 150)
    private String assunto;

    @Column(name = "RESUMO_SESSAO", length = 1000)
    private String resumoSessao;

    @Column(name = "PROXIMOS_PASSOS", length = 1000)
    private String proximosPassos;

    @Column(name = "HUMOR_DETECTADO", length = 50)
    private String humorDetectado;
}
