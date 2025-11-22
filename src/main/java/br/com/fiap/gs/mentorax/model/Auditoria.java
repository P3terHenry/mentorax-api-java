package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_MENTORAX_AUDITORIA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AUDITORIA")
    private Long idAuditoria;

    @Column(name = "NOME_TABELA", length = 100)
    private String nomeTabela;

    @Column(name = "OPERACAO", length = 20)
    private String operacao;

    @Column(name = "USUARIO_OPERACAO", length = 100)
    private String usuarioOperacao;

    @Column(name = "DATA_OPERACAO")
    private LocalDateTime dataOperacao;

    @Lob
    @Column(name = "VALOR_ANTIGO")
    private String valorAntigo;

    @Lob
    @Column(name = "VALOR_NOVO")
    private String valorNovo;
}
