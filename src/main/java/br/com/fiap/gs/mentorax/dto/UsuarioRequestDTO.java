package br.com.fiap.gs.mentorax.dto;

import br.com.fiap.gs.mentorax.model.EnumTipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @Schema(description = "Nome completo do usuário", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Tipo de usuário no sistema", example = "MENTORADO", requiredMode = Schema.RequiredMode.REQUIRED)
    private EnumTipoUsuario tipoUsuario;

    @Schema(description = "Cargo do usuário", example = "Desenvolvedor", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cargo;

    @Schema(description = "Status de ativação do usuário", example = "S", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ativo;

}
