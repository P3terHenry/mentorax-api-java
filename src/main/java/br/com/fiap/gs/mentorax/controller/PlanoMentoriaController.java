package br.com.fiap.gs.mentorax.controller;

import br.com.fiap.gs.mentorax.dto.CriarPlanoMentoriaRequestDTO;
import br.com.fiap.gs.mentorax.dto.CriarPlanoMentoriaResponseDTO;
import br.com.fiap.gs.mentorax.dto.PlanoMentoriaDTO;
import br.com.fiap.gs.mentorax.model.Mentoria;
import br.com.fiap.gs.mentorax.model.PlanoMentoria;
import br.com.fiap.gs.mentorax.repository.MentoriaRepository;
import br.com.fiap.gs.mentorax.repository.PlanoMentoriaRepository;
import br.com.fiap.gs.mentorax.security.JWTUtil;
import br.com.fiap.gs.mentorax.service.PlanoMentoriaService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plano")
@Tag(name = "Plano de Mentoria", description = "Operações relacionadas aos Planos de Mentoria (manual ou com IA).")
public class PlanoMentoriaController {

    @Autowired
    private PlanoMentoriaRepository planoMentoriaRepository;

    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Autowired
    private PlanoMentoriaService planoMentoriaService;

    @Autowired
    private JWTUtil jwtUtil;

    @Operation(summary = "Cria um plano de mentoria. Pode ser manual ou gerado com IA. Apenas o mentor pode criar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plano criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Apenas o mentor pode criar planos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Mentoria não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar plano com IA", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/criar")
    @SecurityRequirement(name = "Bearer Authentication")
    public CriarPlanoMentoriaResponseDTO criarPlano(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CriarPlanoMentoriaRequestDTO request) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        // Buscar mentoria
        Mentoria mentoria = mentoriaRepository.findById(request.getIdMentoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentoria não encontrada."));

        // Verificar se o usuário é o mentor
        if (!mentoria.getMentor().getIdUsuario().equals(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o mentor pode criar planos para esta mentoria.");
        }

        PlanoMentoria novoPlano = new PlanoMentoria();
        novoPlano.setMentoria(mentoria);
        novoPlano.setDataGeracao(LocalDateTime.now());

        boolean geradoComIA = request.getGerarComIA() != null && request.getGerarComIA();

        if (geradoComIA) {
            try {
                // Gerar plano com IA
                String planoCompleto = planoMentoriaService.gerarPlanoComIA(mentoria);
                String[] secoes = planoMentoriaService.extrairSecoes(planoCompleto);

                novoPlano.setCronograma(secoes[0]);
                novoPlano.setDescricaoGeral(secoes[1]);
                novoPlano.setMetasOkr(secoes[2]);
                novoPlano.setMensagemMotivacional(secoes[3]);

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro ao gerar plano com IA: " + e.getMessage());
            }
        } else {
            // Plano manual
            if (request.getCronograma() == null || request.getCronograma().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Para criar um plano manual, forneça todos os campos obrigatórios.");
            }

            novoPlano.setCronograma(request.getCronograma());
            novoPlano.setDescricaoGeral(request.getDescricaoGeral());
            novoPlano.setMetasOkr(request.getMetasOkr());
            novoPlano.setMensagemMotivacional(request.getMensagemMotivacional());
        }

        PlanoMentoria planoSalvo = planoMentoriaRepository.save(novoPlano);

        return new CriarPlanoMentoriaResponseDTO(
                planoSalvo.getIdPlano(),
                mentoria.getIdMentoria(),
                planoSalvo.getDataGeracao(),
                geradoComIA,
                "Plano de mentoria criado com sucesso."
        );
    }

    @Operation(summary = "Retorna todos os planos de uma mentoria. Mentor e mentorado podem acessar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planos retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Você não tem acesso a esta mentoria", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum plano encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/mentoria/{idMentoria}")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<PlanoMentoriaDTO> retornarPlanosPorMentoria(
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem acesso aos planos desta mentoria.");
        }

        List<PlanoMentoria> planos = planoMentoriaRepository.findByMentoriaIdMentoria(idMentoria);

        if (planos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum plano encontrado para esta mentoria.");
        }

        return planos.stream()
                .map(p -> new PlanoMentoriaDTO(
                        p.getIdPlano(),
                        p.getMentoria().getIdMentoria(),
                        p.getCronograma(),
                        p.getDescricaoGeral(),
                        p.getMetasOkr(),
                        p.getMensagemMotivacional(),
                        p.getDataGeracao()
                ))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Retorna o plano mais recente de uma mentoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plano retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Você não tem acesso a esta mentoria", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum plano encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/mentoria/{idMentoria}/atual")
    @SecurityRequirement(name = "Bearer Authentication")
    public PlanoMentoriaDTO retornarPlanoAtual(
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem acesso aos planos desta mentoria.");
        }

        PlanoMentoria plano = planoMentoriaRepository.findFirstByMentoriaIdMentoriaOrderByDataGeracaoDesc(idMentoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum plano encontrado para esta mentoria."));

        return new PlanoMentoriaDTO(
                plano.getIdPlano(),
                plano.getMentoria().getIdMentoria(),
                plano.getCronograma(),
                plano.getDescricaoGeral(),
                plano.getMetasOkr(),
                plano.getMensagemMotivacional(),
                plano.getDataGeracao()
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
