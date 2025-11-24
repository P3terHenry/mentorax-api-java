package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.HistoricoIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoIARepository extends JpaRepository<HistoricoIA, Long> {

    // Buscar histórico por usuário
    List<HistoricoIA> findByUsuarioIdUsuario(Long idUsuario);

    // Buscar histórico por usuário e tipo
    List<HistoricoIA> findByUsuarioIdUsuarioAndTipoGeracao(Long idUsuario, String tipoGeracao);

    // Buscar histórico por usuário ordenado por data
    List<HistoricoIA> findByUsuarioIdUsuarioOrderByDataExecucaoDesc(Long idUsuario);
}

