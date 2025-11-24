package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.PlanoMentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoMentoriaRepository extends JpaRepository<PlanoMentoria, Long> {

    // Buscar planos por mentoria
    List<PlanoMentoria> findByMentoriaIdMentoria(Long idMentoria);

    // Buscar plano mais recente de uma mentoria
    Optional<PlanoMentoria> findFirstByMentoriaIdMentoriaOrderByDataGeracaoDesc(Long idMentoria);
}

