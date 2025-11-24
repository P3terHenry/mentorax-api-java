package br.com.fiap.gs.mentorax.controller;

import br.com.fiap.gs.mentorax.dto.*;
import br.com.fiap.gs.mentorax.model.Mentoria;
import br.com.fiap.gs.mentorax.model.SessaoMentoria;
import br.com.fiap.gs.mentorax.repository.MentoriaRepository;
import br.com.fiap.gs.mentorax.repository.SessaoMentoriaRepository;
import br.com.fiap.gs.mentorax.security.JWTUtil;
import br.com.fiap.gs.mentorax.service.SessaoMentoriaCachingService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessao")
@Tag(name = "Sessão de Mentoria", description = "Operações relacionadas às Sessões de Mentoria.")
public class SessaoMentoriaController {

    @Autowired
    private SessaoMentoriaRepository sessaoMentoriaRepository;

    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SessaoMentoriaCachingService sessaoMentoriaCachingService;

    @Operation(summary = "Cria uma nova sessão de mentoria. Apenas o mentor pode criar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Apenas o mentor pode criar sessões", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Mentoria não encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/criar")
    @SecurityRequirement(name = "Bearer Authentication")
    public CriarSessaoMentoriaResponseDTO criarSessao(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CriarSessaoMentoriaRequestDTO request) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        // Buscar mentoria
        Mentoria mentoria = mentoriaRepository.findById(request.getIdMentoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentoria não encontrada."));

        // Verificar se o usuário é o mentor
        if (!mentoria.getMentor().getIdUsuario().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o mentor pode criar sessões para esta mentoria.");
        }

        // Criar sessão
        SessaoMentoria novaSessao = new SessaoMentoria();
        novaSessao.setMentoria(mentoria);
        novaSessao.setDataSessao(LocalDateTime.now());
        novaSessao.setAssunto(request.getAssunto());
        novaSessao.setResumoSessao(request.getResumoSessao());
        novaSessao.setProximosPassos(request.getProximosPassos());
        novaSessao.setHumorDetectado(request.getHumorDetectado());

        SessaoMentoria sessaoSalva = sessaoMentoriaRepository.save(novaSessao);

        // Limpar cache
        sessaoMentoriaCachingService.limparCache();

        return new CriarSessaoMentoriaResponseDTO(
                sessaoSalva.getIdSessao(),
                mentoria.getIdMentoria(),
                sessaoSalva.getAssunto(),
                sessaoSalva.getDataSessao(),
                "Sessão de mentoria criada com sucesso."
        );
    }

    @Operation(summary = "Retorna todas as sessões de uma mentoria. Mentor e mentorado podem acessar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessões retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Você não tem acesso a esta mentoria", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma sessão encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/mentoria/{idMentoria}")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<SessaoMentoriaDTO> retornarSessoesPorMentoria(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long idMentoria) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        // Buscar mentoria
        Mentoria mentoria = mentoriaRepository.findById(idMentoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentoria não encontrada."));

        // Verificar se o usuário é mentor ou mentorado
        boolean isMentor = mentoria.getMentor().getIdUsuario().equals(idUsuario);
        boolean isMentorado = mentoria.getMentorado().getIdUsuario().equals(idUsuario);

        if (!isMentor && !isMentorado) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem acesso às sessões desta mentoria.");
        }

        List<SessaoMentoriaDTO> sessoes = sessaoMentoriaCachingService.findSessoesPorMentoriaCaching(idMentoria);

        if (sessoes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma sessão encontrada para esta mentoria.");
        }

        return sessoes;
    }

    private Long extrairIdUsuarioDoToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cabeçalho Authorization não informado.");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cabeçalho Authorization deve ter o formato: Bearer <token>");
        }

        String token = authorizationHeader.substring(7).trim();

        if (!jwtUtil.validarToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado.");
        }

        try {
            return jwtUtil.extrairUsuariobyId(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não foi possível extrair o id do token: " + e.getMessage());
        }
    }
}

