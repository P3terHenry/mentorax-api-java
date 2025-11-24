package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.Mentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {

    // Buscar mentorias por mentor
    List<Mentoria> findByMentorIdUsuario(Long idMentor);

    // Buscar mentorias por mentorado
    List<Mentoria> findByMentoradoIdUsuario(Long idMentorado);

    // Buscar mentorias ativas
    List<Mentoria> findByStatus(String status);

    // Buscar mentorias por mentor e status
    List<Mentoria> findByMentorIdUsuarioAndStatus(Long idMentor, String status);

    // Buscar mentorias por mentorado e status
    List<Mentoria> findByMentoradoIdUsuarioAndStatus(Long idMentorado, String status);

    // Verificar se já existe mentoria ativa entre mentor e mentorado
    @Query("SELECT m FROM Mentoria m WHERE m.mentor.idUsuario = :idMentor AND m.mentorado.idUsuario = :idMentorado AND m.status = 'ATIVA'")
    Optional<Mentoria> findMentoriaAtivaEntreUsuarios(Long idMentor, Long idMentorado);
}

