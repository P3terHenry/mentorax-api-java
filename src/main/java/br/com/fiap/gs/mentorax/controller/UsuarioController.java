package br.com.fiap.gs.mentorax.controller;

import br.com.fiap.gs.mentorax.dto.RecuperacaoResponseDTO;
import br.com.fiap.gs.mentorax.dto.RecuperarSenhaRequestDTO;
import br.com.fiap.gs.mentorax.dto.UsuarioDTO;
import br.com.fiap.gs.mentorax.dto.UsuarioRequestDTO;
import br.com.fiap.gs.mentorax.model.EnumTipoUsuario;
import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import br.com.fiap.gs.mentorax.service.UsuarioCachingService;
import br.com.fiap.gs.mentorax.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuários", description = "Operações relacionadas aos Usuários.")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioCachingService usuarioCachingService;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Retorna todas os usuários cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/todos")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<UsuarioDTO> retornaTodosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioRepository.findAllUsuarios();
        if (usuarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado.");
        }
        return usuarios;
    }

    @Operation(summary = "Retorna todos os usuários cacheadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários em cache retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado em cache", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/todos-cachable")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<UsuarioDTO> retornaTodosOsUsuariosCacheable() {
        List<UsuarioDTO> usuarios = usuarioCachingService.findAllUsuariosCaching();
        if (usuarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário em cache.");
        }
        return usuarios;
    }

    @Operation(summary = "Retorna os usuários paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários paginados retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado na página solicitada", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/todos-paginados")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Page<UsuarioDTO>> paginarUsuarios(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size) {

        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetros de paginação inválidos.");
        }

        PageRequest pr = PageRequest.of(page, size);
        Page<UsuarioDTO> paginas_usuarios_dto = usuarioService.paginarTodosOsUsuarios(pr);

        if (paginas_usuarios_dto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado na página solicitada.");
        }

        return ResponseEntity.ok(paginas_usuarios_dto);
    }

    @Operation(summary = "Atualiza um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do usuário", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Usuário para atualização não encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/atualizar/{email}")
    @SecurityRequirement(name = "Bearer Authentication")
    public Usuario atualizarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO, @PathVariable String email) {
        Optional<Usuario> op = usuarioRepository.findByEmail(email);

        if (op.isPresent()) {
            Usuario usuarioExistente = op.get();

            if (usuarioRequestDTO.getNome() == null ||
                    usuarioRequestDTO.getEmail() == null ||
                    usuarioRequestDTO.getTipoUsuario() == null ||
                    usuarioRequestDTO.getCargo() == null ||
                    usuarioRequestDTO.getAtivo() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os campos são obrigatórios para atualização.");
            }

            usuarioExistente.setNome(usuarioRequestDTO.getNome());
            usuarioExistente.setEmail(usuarioRequestDTO.getEmail());
            usuarioExistente.setTipoUsuario(usuarioRequestDTO.getTipoUsuario());
            usuarioExistente.setCargo(usuarioRequestDTO.getCargo());
            usuarioExistente.setAtivo(usuarioRequestDTO.getAtivo());

            usuarioRepository.save(usuarioExistente);
            usuarioCachingService.limparCache();

            return usuarioExistente;

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com email " + email + " não encontrado.");
        }
    }

    @Operation(summary = "Atualiza a senha de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do usuário", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Usuário para atualização não encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/atualizar/senha/{email}")
    @SecurityRequirement(name = "Bearer Authentication")
    public RecuperacaoResponseDTO atualizarSenha(@RequestBody RecuperarSenhaRequestDTO recuperarSenhaRequestDTO, @PathVariable String email) {
        Optional<Usuario> op = usuarioRepository.findByEmail(email);

        if (op.isPresent()) {
            Usuario usuarioExistente = op.get();

            if (recuperarSenhaRequestDTO.getNovaSenha() == null ||
                    recuperarSenhaRequestDTO.getConfirmacaoNovaSenha() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os campos são obrigatórios para atualização da senha.");
            }

            if (!recuperarSenhaRequestDTO.getNovaSenha().equals(recuperarSenhaRequestDTO.getConfirmacaoNovaSenha())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A nova senha e a confirmação de senha não coincidem.");
            }

            usuarioExistente.setSenhaHash(new BCryptPasswordEncoder().encode(recuperarSenhaRequestDTO.getNovaSenha()));

            usuarioRepository.save(usuarioExistente);
            usuarioCachingService.limparCache();

            return new RecuperacaoResponseDTO(
                    "Senha atualizada com sucesso para o usuário: " + usuarioExistente.getEmail()
            );

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com email " + email + " não encontrado.");
        }
    }

    @Operation(summary = "Remove um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/remover/{email}")
    @SecurityRequirement(name = "Bearer Authentication")
    public Usuario removerUsuario(@PathVariable String email) {
        Optional<Usuario> op = usuarioRepository.findByEmail(email);

        if (op.isPresent()) {
            Usuario usuario = op.get();

            usuario.setAtivo("N");
            usuarioCachingService.limparCache();

            return usuarioRepository.save(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com email " + email + " não encontrado.");
        }
    }
}
