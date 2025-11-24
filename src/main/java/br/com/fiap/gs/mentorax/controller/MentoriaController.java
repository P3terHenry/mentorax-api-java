package br.com.fiap.gs.mentorax.controller;

import br.com.fiap.gs.mentorax.dto.*;
import br.com.fiap.gs.mentorax.model.Mentoria;
import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.MentoriaRepository;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import br.com.fiap.gs.mentorax.security.JWTUtil;
import br.com.fiap.gs.mentorax.service.MentoriaCachingService;
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
@RequestMapping("/api/mentoria")
@Tag(name = "Mentoria", description = "Operações relacionadas às Mentorias.")
public class MentoriaController {

    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MentoriaCachingService mentoriaCachingService;

    @Operation(summary = "Cria uma nova mentoria (usuário autenticado será o mentor).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mentoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Já existe mentoria ativa entre esses usuários", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/criar")
    @SecurityRequirement(name = "Bearer Authentication")
    public CriarMentoriaResponseDTO criarMentoria(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CriarMentoriaRequestDTO request) {

        Long idMentor = extrairIdUsuarioDoToken(authorizationHeader);

        // Buscar mentor
        Usuario mentor = usuarioRepository.findById(idMentor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor não encontrado."));

        // Buscar mentorado
        Usuario mentorado = usuarioRepository.findById(request.getIdMentorado())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentorado não encontrado."));

        // Verificar se não é o mesmo usuário
        if (idMentor.equals(request.getIdMentorado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mentor e mentorado não podem ser a mesma pessoa.");
        }

        // Verificar se já existe mentoria ativa
        if (mentoriaRepository.findMentoriaAtivaEntreUsuarios(idMentor, request.getIdMentorado()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma mentoria ativa entre esses usuários.");
        }

        // Criar mentoria
        Mentoria novaMentoria = new Mentoria();
        novaMentoria.setMentor(mentor);
        novaMentoria.setMentorado(mentorado);
        novaMentoria.setDataInicio(LocalDateTime.now());
        novaMentoria.setStatus("ATIVA");

        Mentoria mentoriaSalva = mentoriaRepository.save(novaMentoria);

        // Limpar cache
        mentoriaCachingService.limparCache();

        return new CriarMentoriaResponseDTO(
                mentoriaSalva.getIdMentoria(),
                mentor.getIdUsuario(),
                mentor.getNome(),
                mentorado.getIdUsuario(),
                mentorado.getNome(),
                mentoriaSalva.getStatus(),
                mentoriaSalva.getDataInicio(),
                "Mentoria criada com sucesso."
        );
    }

    @Operation(summary = "Retorna as mentorias do usuário autenticado como mentor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentorias retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma mentoria encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/minhasMentorias/comoMentor")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<MentoriaDTO> retornarMentoriasComoMentor(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        Long idMentor = extrairIdUsuarioDoToken(authorizationHeader);
        List<MentoriaDTO> mentorias = mentoriaCachingService.findMentoriasPorMentorCaching(idMentor);

        if (mentorias.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma mentoria encontrada onde você é mentor.");
        }

        return mentorias;
    }

    @Operation(summary = "Retorna as mentorias do usuário autenticado como mentorado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentorias retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma mentoria encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/minhasMentorias/comoMentorado")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<MentoriaDTO> retornarMentoriasComoMentorado(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        Long idMentorado = extrairIdUsuarioDoToken(authorizationHeader);
        List<MentoriaDTO> mentorias = mentoriaCachingService.findMentoriasPorMentoradoCaching(idMentorado);

        if (mentorias.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma mentoria encontrada onde você é mentorado.");
        }

        return mentorias;
    }

    @Operation(summary = "Atualiza uma mentoria (status e nota). Apenas o mentor pode atualizar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentoria atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Apenas o mentor pode atualizar a mentoria", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Mentoria não encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/atualizar/{idMentoria}")
    @SecurityRequirement(name = "Bearer Authentication")
    public AtualizarMentoriaResponseDTO atualizarMentoria(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long idMentoria,
            @Valid @RequestBody AtualizarMentoriaRequestDTO request) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        // Buscar mentoria
        Mentoria mentoria = mentoriaRepository.findById(idMentoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentoria não encontrada."));

        // Verificar se o usuário é o mentor
        if (!mentoria.getMentor().getIdUsuario().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o mentor pode atualizar esta mentoria.");
        }

        // Validar status
        if (!request.getStatus().matches("ATIVA|PAUSADA|CONCLUIDA")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido. Use: ATIVA, PAUSADA ou CONCLUIDA.");
        }

        // Atualizar mentoria
        mentoria.setStatus(request.getStatus());
        if (request.getNotaSatisfacao() != null) {
            mentoria.setNotaSatisfacao(request.getNotaSatisfacao());
        }
        if ("CONCLUIDA".equals(request.getStatus()) && mentoria.getDataFim() == null) {
            mentoria.setDataFim(LocalDateTime.now());
        }

        Mentoria mentoriaAtualizada = mentoriaRepository.save(mentoria);

        // Limpar cache
        mentoriaCachingService.limparCache();

        return new AtualizarMentoriaResponseDTO(
                mentoriaAtualizada.getIdMentoria(),
                mentoriaAtualizada.getStatus(),
                "Mentoria atualizada com sucesso."
        );
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

