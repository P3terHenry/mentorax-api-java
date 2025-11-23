package br.com.fiap.gs.mentorax.security;

import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Rotas sempre ignoradas
    private static final List<String> EXCLUDED_PREFIXES = List.of(
            "/swagger-ui/",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars",
            "/configuration",
            "/api/auth/"
    );

    private boolean shouldApplyJwt(String path) {
        // Só queremos JWT nas rotas de API
        if (!path.startsWith("/api/")) return false;

        // Excluir prefixos públicos
        for (String p : EXCLUDED_PREFIXES) {
            if (path.startsWith(p)) return false;
        }
        return true;
    }

    private void writeError(HttpServletResponse response, String message, int statusCode, String path) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("""
            {
              "timestamp": "%s",
              "status": %d,
              "error": "%s",
              "message": "%s",
              "path": "%s"
            }
            """, LocalDateTime.now(), statusCode, HttpStatus.valueOf(statusCode).getReasonPhrase(), message, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!shouldApplyJwt(path)) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            writeError(response,
                    "Token JWT obrigatório. Use: Authorization: Bearer <token>",
                    HttpStatus.UNAUTHORIZED.value(), path);
            return;
        }

        String token = header.substring(7);

        try {
            if (!jwtUtil.validarToken(token)) {
                writeError(response, "Token JWT inválido ou expirado.", HttpStatus.FORBIDDEN.value(), path);
                return;
            }

            Long userId = jwtUtil.extrairUsuariobyId(token);
            if (userId == null) {
                writeError(response, "Token sem subject (usuário).", HttpStatus.FORBIDDEN.value(), path);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                Usuario usuario = usuarioRepository.findById(userId).orElse(null);
                if (usuario == null) {
                    writeError(response, "Usuário do token não encontrado.", HttpStatus.FORBIDDEN.value(), path);
                    return;
                }

                UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            writeError(response, "Erro ao processar token JWT: " + e.getMessage(),
                    HttpStatus.FORBIDDEN.value(), path);
            return;
        }

        chain.doFilter(request, response);
    }
}