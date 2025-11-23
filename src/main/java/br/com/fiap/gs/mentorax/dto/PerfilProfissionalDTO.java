package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilProfissionalDTO {

    @Schema(description = "ID do perfil profissional", example = "1")
    private Long idPerfilProfissional;

    @Schema(description = "ID do usuário associado ao perfil", example = "5")
    private Long idUsuario;

    @Schema(description = "Área de interesse profissional", example = "Desenvolvimento de Software")
    private String areaInteresse;

    @Schema(description = "Objetivos profissionais do usuário", example = "Tornar-me especialista em arquitetura de software")
    private String objetivosProfissionais;

    @Schema(description = "Resumo da experiência profissional", example = "5 anos de experiência em desenvolvimento web")
    private String experienciaResumida;

    @Schema(description = "Habilidades comportamentais", example = "Comunicação, liderança, trabalho em equipe")
    private String softSkills;

    @Schema(description = "Habilidades técnicas", example = "Java, Spring Boot, SQL, React")
    private String hardSkills;

    @Schema(description = "Horas disponíveis por semana para mentorias", example = "10")
    private Integer disponibilidadeHoras;

}
