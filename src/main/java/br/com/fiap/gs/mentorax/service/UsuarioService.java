package br.com.fiap.gs.mentorax.service;

import br.com.fiap.gs.mentorax.dto.UsuarioDTO;
import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioCachingService usuarioCachingService;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> paginarTodosOsUsuarios(PageRequest req) {

        Page<Usuario> paginas_usuarios = usuarioCachingService.findAllUsuariosPages(req);
        Page<UsuarioDTO> paginas_usuarios_dto =
                paginas_usuarios.map(UsuarioDTO::new);
        return paginas_usuarios_dto;
    }

}
