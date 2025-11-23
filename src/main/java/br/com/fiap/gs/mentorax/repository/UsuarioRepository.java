package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.dto.UsuarioDTO;
import br.com.fiap.gs.mentorax.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
    SELECT new br.com.fiap.gs.mentorax.dto.UsuarioDTO(
        u.idUsuario,
        u.nome,
        u.email,
        u.tipoUsuario,
        u.cargo,
        u.dataCadastro,
        u.ativo
    )
    FROM Usuario u
    """)
    List<UsuarioDTO> findAllUsuarios();
}
