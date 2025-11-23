package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletarPerfilProfissionalResponseDTO {

    @Schema(description = "ID do perfil profissional deletado", example = "1")
    private Long idPerfil;

    @Schema(description = "ID do usuário associado ao perfil", example = "5")
    private Long idUsuario;

    @Schema(description = "Mensagem de sucesso", example = "Perfil profissional deletado com sucesso.")
    private String mensagem;

}

