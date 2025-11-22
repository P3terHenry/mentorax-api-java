package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.Mentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {

    List<Mentoria> findByMentorIdUsuario(Long idMentor);

    List<Mentoria> findByMentoradoIdUsuario(Long idMentorado);
}
