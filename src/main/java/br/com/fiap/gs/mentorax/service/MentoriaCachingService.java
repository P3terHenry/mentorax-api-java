package br.com.fiap.gs.mentorax.service;

import br.com.fiap.gs.mentorax.dto.MentoriaDTO;
import br.com.fiap.gs.mentorax.model.Mentoria;
import br.com.fiap.gs.mentorax.repository.MentoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MentoriaCachingService {

    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Cacheable(value = "mentoriasPorMentor", key = "#idMentor")
    public List<MentoriaDTO> findMentoriasPorMentorCaching(Long idMentor) {
        List<Mentoria> mentorias = mentoriaRepository.findByMentorIdUsuario(idMentor);
        return mentorias.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "mentoriasPorMentorado", key = "#idMentorado")
    public List<MentoriaDTO> findMentoriasPorMentoradoCaching(Long idMentorado) {
        List<Mentoria> mentorias = mentoriaRepository.findByMentoradoIdUsuario(idMentorado);
        return mentorias.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"mentoriasPorMentor", "mentoriasPorMentorado"}, allEntries = true)
    public void limparCache() {
        System.out.println("Limpando cache de mentorias!");
    }

    private MentoriaDTO convertToDTO(Mentoria mentoria) {
        return new MentoriaDTO(
                mentoria.getIdMentoria(),
                mentoria.getMentor().getIdUsuario(),
                mentoria.getMentor().getNome(),
                mentoria.getMentor().getEmail(),
                mentoria.getMentorado().getIdUsuario(),
                mentoria.getMentorado().getNome(),
                mentoria.getMentorado().getEmail(),
                mentoria.getDataInicio(),
                mentoria.getDataFim(),
                mentoria.getStatus(),
                mentoria.getNotaSatisfacao()
        );
    }
}

