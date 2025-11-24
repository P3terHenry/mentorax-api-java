package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.SessaoMentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessaoMentoriaRepository extends JpaRepository<SessaoMentoria, Long> {

    // Buscar sessões por mentoria
    List<SessaoMentoria> findByMentoriaIdMentoria(Long idMentoria);

    // Buscar sessões por mentoria ordenadas por data
    List<SessaoMentoria> findByMentoriaIdMentoriaOrderByDataSessaoDesc(Long idMentoria);
}

