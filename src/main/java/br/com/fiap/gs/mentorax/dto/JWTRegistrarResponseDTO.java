package br.com.fiap.gs.mentorax.dto;

import br.com.fiap.gs.mentorax.model.EnumTipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTRegistrarResponseDTO {

    @Schema(description = "Mensagem de confirmação", example = "Usuário criado com sucesso.")
    private String mensagem;

    @Schema(description = "E-mail do usuário cadastrado", example = "usuario@exemplo.com")
    private String email;

    @Schema(description = "Tipo de usuário cadastrado", example = "MENTORADO")
    private EnumTipoUsuario tipoUsuario;
}
