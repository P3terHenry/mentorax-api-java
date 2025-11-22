package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.PlanoMentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanoMentoriaRepository extends JpaRepository<PlanoMentoria, Long> {

    List<PlanoMentoria> findByMentoriaIdMentoria(Long idMentoria);
}
