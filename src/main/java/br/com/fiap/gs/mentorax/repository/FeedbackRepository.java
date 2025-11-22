package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findBySessaoIdSessao(Long idSessao);
}
