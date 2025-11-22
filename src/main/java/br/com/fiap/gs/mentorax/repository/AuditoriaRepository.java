package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
}
