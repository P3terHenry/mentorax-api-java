package br.com.fiap.gs.mentorax.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_MENTORAX_MENTORIA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MENTORIA")
    private Long idMentoria;

    @ManyToOne
    @JoinColumn(name = "ID_MENTOR",referencedColumnName = "ID_USUARIO", nullable = false)
    private Usuario mentor;

    @ManyToOne
    @JoinColumn(name = "ID_MENTORADO", referencedColumnName = "ID_USUARIO", nullable = false)
    private Usuario mentorado;

    @Column(name = "DATA_INICIO")
    private LocalDateTime dataInicio;

    @Column(name = "DATA_FIM")
    private LocalDateTime dataFim;

    @Column(length = 20)
    private String status;

    @Column(name = "NOTA_SATISFACAO", precision = 3, scale = 1)
    private BigDecimal notaSatisfacao;
}
