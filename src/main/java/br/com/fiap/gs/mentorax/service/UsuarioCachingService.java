package br.com.fiap.gs.mentorax.service;

import br.com.fiap.gs.mentorax.dto.UsuarioDTO;
import br.com.fiap.gs.mentorax.model.Usuario;
import br.com.fiap.gs.mentorax.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioCachingService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Cacheable(value = "findAllUsuariosCaching")
    public List<UsuarioDTO> findAllUsuariosCaching() {
        return usuarioRepository.findAllUsuarios();
    }

    @Cacheable(value = "findAllUsuariosPages", key = "#req")
    public Page<Usuario> findAllUsuariosPages(PageRequest req) {
        return usuarioRepository.findAll(req);
    }

    @CacheEvict(value = {"findAllUsuariosCaching", "findAllUsuariosPages"}, allEntries = true)
    public void limparCache() {
        System.out.println("Limpando cache!");
    }
}
