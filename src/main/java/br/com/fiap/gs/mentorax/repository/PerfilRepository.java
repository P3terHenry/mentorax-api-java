package br.com.fiap.gs.mentorax.repository;

import br.com.fiap.gs.mentorax.model.PerfilProfissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilProfissional, Long> {

    List<PerfilProfissional> findByAreaInteresseContainingIgnoreCase(String areaInteresse);
}
