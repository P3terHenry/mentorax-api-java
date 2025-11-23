package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CriarPerfilProfissionalResponseDTO {

    @Schema(description = "ID do perfil profissional criado")
    private Long idPerfil;

    @Schema(description = "ID do usuário associado ao perfil")
    private Long idUsuario;

    @Schema(description = "Mensagem de sucesso")
    private String mensagem;

}

