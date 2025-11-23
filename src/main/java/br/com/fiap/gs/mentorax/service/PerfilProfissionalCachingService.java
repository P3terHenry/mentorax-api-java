package br.com.fiap.gs.mentorax.service;

import br.com.fiap.gs.mentorax.dto.PerfilProfissionalDTO;
import br.com.fiap.gs.mentorax.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilProfissionalCachingService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Cacheable(value = "findAllPerfisProfissionalCaching")
    public List<PerfilProfissionalDTO> findAllPerfisProfissionalCaching() {
        return perfilRepository.findAllPerfis();
    }

    @CacheEvict(value = {"findAllPerfisProfissionalCaching"}, allEntries = true)
    public void limparCache() {
        System.out.println("Limpando cache!");
    }
}
