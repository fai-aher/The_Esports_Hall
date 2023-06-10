package co.edu.uniandes.dse.esports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

@Repository
public interface CompetenciaRepository extends JpaRepository<CompetenciaEntity, Long> {
    CompetenciaEntity findByJugadorIdAndId(Long jugadorId, Long id);
}