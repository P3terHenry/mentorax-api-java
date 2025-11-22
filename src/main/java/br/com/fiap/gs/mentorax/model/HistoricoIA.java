package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_MENTORAX_HISTORICO_IA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HISTORICO")
    private Long idHistorico;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private Usuario usuario;

    @Lob
    @Column(name = "PROMPT_ENVIADO")
    private String promptEnviado;

    @Lob
    @Column(name = "RESPOSTA_IA")
    private String respostaIa;

    @Column(name = "DATA_EXECUCAO")
    private LocalDateTime dataExecucao;

    @Column(name = "TIPO_GERACAO", length = 50)
    private String tipoGeracao;
}
