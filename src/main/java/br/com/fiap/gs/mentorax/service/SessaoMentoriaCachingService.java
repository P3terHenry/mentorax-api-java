package br.com.fiap.gs.mentorax.service;

import br.com.fiap.gs.mentorax.dto.SessaoMentoriaDTO;
import br.com.fiap.gs.mentorax.model.SessaoMentoria;
import br.com.fiap.gs.mentorax.repository.SessaoMentoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessaoMentoriaCachingService {

    @Autowired
    private SessaoMentoriaRepository sessaoMentoriaRepository;

    @Cacheable(value = "sessoesPorMentoria", key = "#idMentoria")
    public List<SessaoMentoriaDTO> findSessoesPorMentoriaCaching(Long idMentoria) {
        List<SessaoMentoria> sessoes = sessaoMentoriaRepository.findByMentoriaIdMentoriaOrderByDataSessaoDesc(idMentoria);
        return sessoes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"sessoesPorMentoria"}, allEntries = true)
    public void limparCache() {
        System.out.println("Limpando cache de sessões de mentoria!");
    }

    private SessaoMentoriaDTO convertToDTO(SessaoMentoria sessao) {
        return new SessaoMentoriaDTO(
                sessao.getIdSessao(),
                sessao.getMentoria().getIdMentoria(),
                sessao.getDataSessao(),
                sessao.getAssunto(),
                sessao.getResumoSessao(),
                sessao.getProximosPassos(),
                sessao.getHumorDetectado()
        );
    }
}

