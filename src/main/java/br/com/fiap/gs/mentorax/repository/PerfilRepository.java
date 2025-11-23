package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.dto.PerfilProfissionalDTO;
import br.com.fiap.gs.mentorax.model.PerfilProfissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilProfissional, Long> {

    List<PerfilProfissional> findByAreaInteresseContainingIgnoreCase(String areaInteresse);

    @Query("""
    SELECT new br.com.fiap.gs.mentorax.dto.PerfilProfissionalDTO(
        p.idPerfil,
        p.usuario.idUsuario,
        p.areaInteresse,
        p.objetivosProfissionais,
        p.experienciaResumida,
        p.softSkills,
        p.hardSkills,
        p.disponibilidadeHoras
    )
    FROM PerfilProfissional p
""")
    List<PerfilProfissionalDTO> findAllPerfis();

    // Busca o perfil profissional associado a um usuário pelo id do usuário
    Optional<PerfilProfissional> findByUsuarioIdUsuario(Long idUsuario);

}
