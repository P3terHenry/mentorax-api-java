package br.com.fiap.gs.mentorax.controller;

import br.com.fiap.gs.mentorax.dto.GerarConteudoIARequestDTO;
import br.com.fiap.gs.mentorax.dto.GerarConteudoIAResponseDTO;
import br.com.fiap.gs.mentorax.dto.HistoricoIADTO;
import br.com.fiap.gs.mentorax.model.HistoricoIA;
import br.com.fiap.gs.mentorax.model.PerfilProfissional;
import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.HistoricoIARepository;
import br.com.fiap.gs.mentorax.repository.PerfilRepository;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import br.com.fiap.gs.mentorax.security.JWTUtil;
import br.com.fiap.gs.mentorax.service.OpenAIService;
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
@RequestMapping("/api/ia")
@Tag(name = "Inteligência Artificial", description = "Operações com IA (OpenAI GPT-4o-mini) para geração de conteúdo personalizado.")
public class IAController {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private HistoricoIARepository historicoIARepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Operation(summary = "Gera conteúdo personalizado usando IA baseado em um prompt livre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteúdo gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro ao chamar a API da OpenAI", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/gerar")
    @SecurityRequirement(name = "Bearer Authentication")
    public GerarConteudoIAResponseDTO gerarConteudo(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody GerarConteudoIARequestDTO request) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        try {
            // Gerar conteúdo com a IA
            String respostaIA = openAIService.gerarConteudo(request.getPrompt());

            // Salvar no histórico
            HistoricoIA historico = new HistoricoIA();
            historico.setUsuario(usuario);
            historico.setPromptEnviado(request.getPrompt());
            historico.setRespostaIa(respostaIA);
            historico.setDataExecucao(LocalDateTime.now());
            historico.setTipoGeracao(request.getTipoGeracao() != null ? request.getTipoGeracao() : "OUTRO");

            HistoricoIA historicoSalvo = historicoIARepository.save(historico);

            return new GerarConteudoIAResponseDTO(
                    historicoSalvo.getIdHistorico(),
                    historicoSalvo.getPromptEnviado(),
                    historicoSalvo.getRespostaIa(),
                    historicoSalvo.getTipoGeracao(),
                    historicoSalvo.getDataExecucao()
            );

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao gerar conteúdo com IA: " + e.getMessage());
        }
    }

    @Operation(summary = "Gera recomendações personalizadas baseadas no perfil profissional do usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recomendações geradas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Perfil profissional não encontrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro ao chamar a API da OpenAI", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/recomendacoes/meuPerfil")
    @SecurityRequirement(name = "Bearer Authentication")
    public GerarConteudoIAResponseDTO gerarRecomendacoesPersonalizadas(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        PerfilProfissional perfil = perfilRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Perfil profissional não encontrado. Crie seu perfil primeiro."));

        try {
            // Gerar recomendações com a IA
            String respostaIA = openAIService.gerarRecomendacoesPersonalizadas(
                    perfil.getAreaInteresse(),
                    perfil.getObjetivosProfissionais(),
                    perfil.getExperienciaResumida(),
                    perfil.getSoftSkills(),
                    perfil.getHardSkills()
            );

            // Salvar no histórico
            HistoricoIA historico = new HistoricoIA();
            historico.setUsuario(usuario);
            historico.setPromptEnviado("Recomendações personalizadas baseadas no perfil profissional");
            historico.setRespostaIa(respostaIA);
            historico.setDataExecucao(LocalDateTime.now());
            historico.setTipoGeracao("RECOMENDACAO");

            HistoricoIA historicoSalvo = historicoIARepository.save(historico);

            return new GerarConteudoIAResponseDTO(
                    historicoSalvo.getIdHistorico(),
                    historicoSalvo.getPromptEnviado(),
                    historicoSalvo.getRespostaIa(),
                    historicoSalvo.getTipoGeracao(),
                    historicoSalvo.getDataExecucao()
            );

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao gerar recomendações com IA: " + e.getMessage());
        }
    }

    @Operation(summary = "Gera um plano de estudos personalizado baseado no perfil do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plano de estudos gerado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Perfil profissional não encontrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro ao chamar a API da OpenAI", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/planoEstudos/meuPerfil")
    @SecurityRequirement(name = "Bearer Authentication")
    public GerarConteudoIAResponseDTO gerarPlanoEstudos(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        PerfilProfissional perfil = perfilRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Perfil profissional não encontrado. Crie seu perfil primeiro."));

        try {
            // Gerar plano de estudos com a IA
            String respostaIA = openAIService.gerarPlanoEstudos(
                    perfil.getAreaInteresse(),
                    perfil.getObjetivosProfissionais(),
                    perfil.getDisponibilidadeHoras()
            );

            // Salvar no histórico
            HistoricoIA historico = new HistoricoIA();
            historico.setUsuario(usuario);
            historico.setPromptEnviado("Plano de estudos personalizado baseado no perfil profissional");
            historico.setRespostaIa(respostaIA);
            historico.setDataExecucao(LocalDateTime.now());
            historico.setTipoGeracao("PLANO_ESTUDO");

            HistoricoIA historicoSalvo = historicoIARepository.save(historico);

            return new GerarConteudoIAResponseDTO(
                    historicoSalvo.getIdHistorico(),
                    historicoSalvo.getPromptEnviado(),
                    historicoSalvo.getRespostaIa(),
                    historicoSalvo.getTipoGeracao(),
                    historicoSalvo.getDataExecucao()
            );

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao gerar plano de estudos com IA: " + e.getMessage());
        }
    }

    @Operation(summary = "Retorna o histórico de chamadas de IA do usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum histórico encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/historico/meu")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<HistoricoIADTO> retornarMeuHistorico(
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        Long idUsuario = extrairIdUsuarioDoToken(authorizationHeader);

        List<HistoricoIA> historicos = historicoIARepository.findByUsuarioIdUsuarioOrderByDataExecucaoDesc(idUsuario);

        if (historicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum histórico de IA encontrado.");
        }

        return historicos.stream()
                .map(h -> new HistoricoIADTO(
                        h.getIdHistorico(),
                        h.getUsuario().getIdUsuario(),
                        h.getUsuario().getNome(),
                        h.getPromptEnviado(),
                        // Resumir resposta se for muito longa
                        h.getRespostaIa().length() > 200
                                ? h.getRespostaIa().substring(0, 200) + "..."
                                : h.getRespostaIa(),
                        h.getTipoGeracao(),
                        h.getDataExecucao()
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

