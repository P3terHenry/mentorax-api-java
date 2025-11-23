package br.com.fiap.gs.mentorax.dto;

import br.com.fiap.gs.mentorax.model.EnumTipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JWTRegistrarRequestDTO {

    @Schema(description = "E-mail do novo usuário", example = "novousuario@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Nome completo do usuário", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "Cargo do usuário", example = "Desenvolvedor", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cargo;

    @Schema(description = "Tipo de usuário no sistema", example = "MENTORADO", requiredMode = Schema.RequiredMode.REQUIRED)
    private EnumTipoUsuario tipoUsuario;

    @Schema(description = "Senha do usuário", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senha;
}
