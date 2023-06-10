package co.edu.uniandes.dse.esports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.esports.entities.LogrosEntity;

@Repository
public interface LogrosRepository extends JpaRepository<LogrosEntity, Long> {
    LogrosEntity findByJugadorIdAndId(Long jugadorId, Long id);
}

