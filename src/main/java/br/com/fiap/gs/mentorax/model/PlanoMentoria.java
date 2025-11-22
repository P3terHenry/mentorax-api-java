package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_MENTORAX_PLANO_MENTORIA")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PlanoMentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PLANO")
    private Long idPlano;

    @ManyToOne
    @JoinColumn(name = "ID_MENTORIA", referencedColumnName = "ID_MENTORIA")
    private Mentoria mentoria;

    @Lob
    @Column(name = "CRONOGRAMA")
    private String cronograma;

    @Column(name = "DESCRICAO_GERAL", length = 1000)
    private String descricaoGeral;

    @Lob
    @Column(name = "METAS_OKR")
    private String metasOkr;

    @Column(name = "MENSAGEM_MOTIVACIONAL", length = 500)
    private String mensagemMotivacional;

    @Column(name = "DATA_GERACAO")
    private LocalDateTime dataGeracao;
}
