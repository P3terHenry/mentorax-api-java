package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarPerfilProfissionalRequestDTO {

    @NotBlank(message = "Área de interesse é obrigatória.")
    @Schema(description = "Área de interesse profissional", example = "Desenvolvimento de Software")
    private String areaInteresse;

    @NotBlank(message = "Objetivos profissionais são obrigatórios.")
    @Schema(description = "Objetivos profissionais do usuário", example = "Tornar-me especialista em arquitetura de software")
    private String objetivosProfissionais;

    @NotBlank(message = "Experiência resumida é obrigatória.")
    @Schema(description = "Resumo da experiência profissional", example = "5 anos de experiência em desenvolvimento web")
    private String experienciaResumida;

    @NotBlank(message = "Soft skills são obrigatórias.")
    @Schema(description = "Habilidades comportamentais", example = "Comunicação, liderança, trabalho em equipe")
    private String softSkills;

    @NotBlank(message = "Hard skills são obrigatórias.")
    @Schema(description = "Habilidades técnicas", example = "Java, Spring Boot, SQL, React")
    private String hardSkills;

    @NotNull(message = "Disponibilidade de horas é obrigatória.")
    @Schema(description = "Horas disponíveis por semana", example = "10")
    private Integer disponibilidadeHoras;

}

