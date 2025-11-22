package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_MENTORAX_FEEDBACK")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FEEDBACK")
    private Long idFeedback;

    @ManyToOne
    @JoinColumn(name = "ID_SESSAO", referencedColumnName = "ID_SESSAO")
    private SessaoMentoria sessao;

    @Column(name = "NOTA")
    private Integer nota;

    @Column(name = "COMENTARIO", length = 500)
    private String comentario;
}
