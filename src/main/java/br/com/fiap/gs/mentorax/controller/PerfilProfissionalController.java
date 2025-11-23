package br.com.fiap.gs.mentorax.controller;

import br.com.fiap.gs.mentorax.dto.CriarPerfilProfissionalRequestDTO;
import br.com.fiap.gs.mentorax.dto.CriarPerfilProfissionalResponseDTO;
import br.com.fiap.gs.mentorax.dto.PerfilProfissionalDTO;
import br.com.fiap.gs.mentorax.model.PerfilProfissional;
import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.PerfilRepository;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import br.com.fiap.gs.mentorax.security.JWTUtil;
import br.com.fiap.gs.mentorax.service.PerfilProfissionalCachingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/perfil")
@Tag(name = "Perfil Profissional", description = "Operações relacionadas ao Perfil Profissional.")
public class PerfilProfissionalController {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PerfilProfissionalCachingService perfilProfissionalCachingService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Operation(summary = "Retorna todas os perfil profissionais cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfis Profissionais retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil profissional encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/todos")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<PerfilProfissionalDTO> retornaTodosPerfisProfissionais() {
        List<PerfilProfissionalDTO> perfil = perfilRepository.findAllPerfis();
        if (perfil.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum perfil profissional encontrado.");
        }
        return perfil;
    }

    @Operation(summary = "Retorna todas os perfil profissionais cacheados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfis Profissionais retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil profissional encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/todos-cachable")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<PerfilProfissionalDTO> retornaTodosPerfisProfissionaisCacheable() {
        List<PerfilProfissionalDTO> perfil = perfilProfissionalCachingService.findAllPerfisProfissionalCaching();
        if (perfil.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum perfil profissional encontrado em cache.");
        }
        return perfil;

    }

    @Operation(summary = "Retorna o perfil profissional do usuário presente no token do header Authorization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil Profissional retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Perfil profissional não encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/meuPerfil")
    @SecurityRequirement(name = "Bearer Authentication")
    public PerfilProfissionalDTO retornaPerfilProfissionalDoIdDoToken(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cabeçalho Authorization não informado.");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cabeçalho Authorization deve ter o formato: Bearer <token>");
        }

        String token = authorizationHeader.substring(7).trim();

        boolean valido = jwtUtil.validarToken(token);
        if (!valido) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado.");
        }

        Long idUsuario;
        try {
            idUsuario = jwtUtil.extrairUsuariobyId(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não foi possível extrair o id do token: " + e.getMessage());
        }

        PerfilProfissional perfil = perfilRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil profissional não encontrado para o usuário do token."));

        return new PerfilProfissionalDTO(
                perfil.getIdPerfil(),
                perfil.getUsuario().getIdUsuario(),
                perfil.getAreaInteresse(),
                perfil.getObjetivosProfissionais(),
                perfil.getExperienciaResumida(),
                perfil.getSoftSkills(),
                perfil.getHardSkills(),
                perfil.getDisponibilidadeHoras()
        );

    }

    @Operation(summary = "Cria um perfil profissional para o usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil profissional criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Usuário já possui um perfil profissional", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(value = "/criar")
    @SecurityRequirement(name = "Bearer Authentication")
    public CriarPerfilProfissionalResponseDTO criarPerfilProfissional(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CriarPerfilProfissionalRequestDTO request) {

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cabeçalho Authorization não informado.");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cabeçalho Authorization deve ter o formato: Bearer <token>");
        }

        String token = authorizationHeader.substring(7).trim();

        boolean valido = jwtUtil.validarToken(token);
        if (!valido) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado.");
        }

        Long idUsuario;
        try {
            idUsuario = jwtUtil.extrairUsuariobyId(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não foi possível extrair o id do token: " + e.getMessage());
        }

        // Verificar se o usuário existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        // Verificar se o usuário já possui um perfil profissional
        if (perfilRepository.findByUsuarioIdUsuario(idUsuario).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já possui um perfil profissional cadastrado.");
        }

        // Criar o perfil profissional
        PerfilProfissional novoPerfil = new PerfilProfissional();
        novoPerfil.setUsuario(usuario);
        novoPerfil.setAreaInteresse(request.getAreaInteresse());
        novoPerfil.setObjetivosProfissionais(request.getObjetivosProfissionais());
        novoPerfil.setExperienciaResumida(request.getExperienciaResumida());
        novoPerfil.setSoftSkills(request.getSoftSkills());
        novoPerfil.setHardSkills(request.getHardSkills());
        novoPerfil.setDisponibilidadeHoras(request.getDisponibilidadeHoras());

        PerfilProfissional perfilSalvo = perfilRepository.save(novoPerfil);

        // Limpar cache
        perfilProfissionalCachingService.limparCache();

        return new CriarPerfilProfissionalResponseDTO(
                perfilSalvo.getIdPerfil(),
                perfilSalvo.getUsuario().getIdUsuario(),
                "Perfil profissional criado com sucesso."
        );
    }

}
