package br.com.fiap.gs.mentorax.service;

import br.com.fiap.gs.mentorax.dto.ValidarCodigoResponseDTO;
import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import br.com.fiap.gs.mentorax.security.JWTUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class RecuperacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    private static final int TEMPO_EXPIRACAO_MINUTOS = 10;
    private static final int MAX_TENTATIVAS = 3;

    @Autowired
    private JWTUtil jWTUtil;

    @Transactional
    public void solicitarRecuperacao(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "E-mail não encontrado."));

        if (usuario.getUltimaRecuperacaoEm() != null &&
                usuario.getUltimaRecuperacaoEm().isAfter(LocalDateTime.now().minusMinutes(5))) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Aguarde alguns minutos antes de solicitar novamente.");
        }

        String codigo = String.format("%06d", new Random().nextInt(999999));

        usuario.setCodigoRecuperacao(codigo);
        usuario.setCodigoRecuperacaoExpiraEm(LocalDateTime.now().plusMinutes(TEMPO_EXPIRACAO_MINUTOS));
        usuario.setCodigoRecuperacaoTentativas(0);
        usuario.setUltimaRecuperacaoEm(LocalDateTime.now());

        usuarioRepository.save(usuario);

        // Envia o e-mail real
        emailService.enviarCodigoRecuperacao(usuario.getEmail(), usuario.getNome(), codigo);
    }

    public ValidarCodigoResponseDTO validarCodigo(String email, String codigo) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "E-mail não encontrado."));

        if (usuario.getCodigoRecuperacao() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum código de recuperação foi solicitado.");

        if (usuario.getCodigoRecuperacaoExpiraEm().isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O código de recuperação expirou.");

        if (usuario.getCodigoRecuperacaoTentativas() >= MAX_TENTATIVAS)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Número máximo de tentativas excedido.");

        if (!usuario.getCodigoRecuperacao().equals(codigo)) {
            incrementarTentativas(usuario);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código incorreto.");
        }

        limparCodigo(usuario);

        String token = jWTUtil.construirToken(usuario);

        ValidarCodigoResponseDTO response = new ValidarCodigoResponseDTO();
        response.setCodigoValido(true);
        response.setToken(token);
        response.setMessage("Código validado com sucesso. Use este token para redefinir sua senha.");

        return response;
    }

    @Transactional
    protected void incrementarTentativas(Usuario usuario) {
        usuario.setCodigoRecuperacaoTentativas(usuario.getCodigoRecuperacaoTentativas() + 1);
        usuarioRepository.save(usuario);
    }

    @Transactional
    protected void limparCodigo(Usuario usuario) {
        usuario.setCodigoRecuperacao(null);
        usuario.setCodigoRecuperacaoExpiraEm(null);
        usuario.setCodigoRecuperacaoTentativas(0);
        usuarioRepository.save(usuario);
    }

}
