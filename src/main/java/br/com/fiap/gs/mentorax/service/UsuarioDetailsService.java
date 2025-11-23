package br.com.fiap.gs.mentorax.service;

import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws ResponseStatusException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Usuário não encontrado com o e-mail: " + email));

        // Define a role com base no tipoUsuario (Enum) e não no cargo (String)
        String role = "ROLE_" + usuario.getTipoUsuario().name(); // ADMIN, MENTOR, MENTORADO

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        return new User(
                usuario.getEmail(),
                usuario.getSenhaHash(),
                authorities
        );
    }
}
