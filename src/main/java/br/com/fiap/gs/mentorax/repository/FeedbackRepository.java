package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Buscar feedback por sessão
    Optional<Feedback> findBySessaoIdSessao(Long idSessao);

    // Buscar todos feedbacks de uma mentoria (via sessões)
    List<Feedback> findBySessaoMentoriaIdMentoria(Long idMentoria);
}

