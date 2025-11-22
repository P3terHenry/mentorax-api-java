package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.HistoricoIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoIARepository extends JpaRepository<HistoricoIA, Long> {

    List<HistoricoIA> findByUsuarioIdUsuario(Long idUsuario);
}
