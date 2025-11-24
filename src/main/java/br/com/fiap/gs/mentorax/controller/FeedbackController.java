package br.com.fiap.gs.mentorax.controller;

import br.com.fiap.gs.mentorax.dto.CriarFeedbackRequestDTO;
import br.com.fiap.gs.mentorax.dto.CriarFeedbackResponseDTO;
import br.com.fiap.gs.mentorax.dto.FeedbackDTO;
import br.com.fiap.gs.mentorax.model.Feedback;
import br.com.fiap.gs.mentorax.model.SessaoMentoria;
import br.com.fiap.gs.mentorax.repository.FeedbackRepository;
import br.com.fiap.gs.mentorax.repository.SessaoMentoriaRepository;
import br.com.fiap.gs.mentorax.security.JWTUtil;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedback")
@Tag(name = "Feedback", description = "Operações relacionadas aos Feedbacks das Sessões.")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SessaoMentoriaRepository sessaoMentoriaRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Operation(summary = "Cria um feedback para uma sessão. Apenas o mentorado pode criar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Apenas o mentorado pode criar feedback", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Feedback já existe para esta sessão", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/criar")
    @SecurityRequirement(name = "Bearer Authentication")
    public CriarFeedbackResponseDTO criarFeedback(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CriarFeedbackRequestDTO request) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        // Buscar sessão
        SessaoMentoria sessao = sessaoMentoriaRepository.findById(request.getIdSessao())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada."));

        // Verificar se o usuário é o mentorado
        if (!sessao.getMentoria().getMentorado().getIdUsuario().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o mentorado pode criar feedback para esta sessão.");
        }

        // Verificar se já existe feedback para esta sessão
        if (feedbackRepository.findBySessaoIdSessao(request.getIdSessao()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um feedback para esta sessão.");
        }

        // Criar feedback
        Feedback novoFeedback = new Feedback();
        novoFeedback.setSessao(sessao);
        novoFeedback.setNota(request.getNota());
        novoFeedback.setComentario(request.getComentario());

        Feedback feedbackSalvo = feedbackRepository.save(novoFeedback);

        return new CriarFeedbackResponseDTO(
                feedbackSalvo.getIdFeedback(),
                sessao.getIdSessao(),
                feedbackSalvo.getNota(),
                "Feedback enviado com sucesso."
        );
    }

    @Operation(summary = "Retorna todos os feedbacks de uma mentoria. Mentor e mentorado podem acessar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedbacks retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Você não tem acesso a esta mentoria", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum feedback encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/mentoria/{idMentoria}")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<FeedbackDTO> retornarFeedbacksPorMentoria(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long idMentoria) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        // Buscar feedbacks
        List<Feedback> feedbacks = feedbackRepository.findBySessaoMentoriaIdMentoria(idMentoria);

        if (feedbacks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum feedback encontrado para esta mentoria.");
        }

        // Verificar se o usuário tem acesso (mentor ou mentorado)
        Feedback primeiroFeedback = feedbacks.get(0);
        boolean isMentor = primeiroFeedback.getSessao().getMentoria().getMentor().getIdUsuario().equals(idUsuario);
        boolean isMentorado = primeiroFeedback.getSessao().getMentoria().getMentorado().getIdUsuario().equals(idUsuario);

        if (!isMentor && !isMentorado) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem acesso aos feedbacks desta mentoria.");
        }

        return feedbacks.stream()
                .map(f -> new FeedbackDTO(
                        f.getIdFeedback(),
                        f.getSessao().getIdSessao(),
                        f.getSessao().getAssunto(),
                        f.getNota(),
                        f.getComentario()
                ))
                .collect(Collectors.toList());
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

