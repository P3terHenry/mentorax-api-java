package br.com.fiap.gs.mentorax.controller;

import br.com.fiap.gs.mentorax.dto.*;
import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import br.com.fiap.gs.mentorax.security.JWTUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas à autenticação.")
public class AuthController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Solicitar token de autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token gerado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/login")
    public JWTLoginResponseDTO gerarToken(@RequestBody JWTLoginRequestDTO login) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getSenha())
            );

            Usuario usuario = usuarioRepository.findByEmail(login.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

            String token = jwtUtil.construirToken(usuario);
            Claims claims = jwtUtil.extrairClaims(token);

            return new JWTLoginResponseDTO(
                    token,
                    claims.getSubject(),
                    claims.getIssuedAt(),
                    claims.getExpiration()
            );

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.");
        }
    }

    @Operation(summary = "Verificar se o token é válido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido."),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao validar token.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/validarToken")
    public JWTValidarTokenResponseDTO verificarToken(@RequestBody JWTValidarTokenRequestDTO token) {
        boolean statusToken;
        try {

            statusToken = jwtUtil.validarToken(token.getToken());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado ao validar o token: " + e.getMessage());
        }

        if (!statusToken) {
            return new JWTValidarTokenResponseDTO(
                    token.getToken(),
                    false,
                    "Token inválido ou expirado."
            );
        }

        return new JWTValidarTokenResponseDTO(
                token.getToken(),
                true,
                "Token válido."
        );
    }

    @Operation(summary = "Registrar novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao registrar usuário.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/registrar")
    public JWTRegistrarResponseDTO registrarUsuario(@RequestBody JWTRegistrarRequestDTO dto) {
        try {
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
            }

            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setSenhaHash(new BCryptPasswordEncoder().encode(dto.getSenha()));
            usuario.setCargo(dto.getCargo());
            usuario.setTipoUsuario(dto.getTipoUsuario());
            usuario.setAtivo("S");
            usuario.setDataCadastro(LocalDateTime.now());

            usuarioRepository.save(usuario);

            return new JWTRegistrarResponseDTO(
                    "Usuário criado com sucesso.",
                    usuario.getEmail(),
                    usuario.getTipoUsuario()
            );
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado ao registrar usuário: " + e.getMessage());
        }
    }
}
