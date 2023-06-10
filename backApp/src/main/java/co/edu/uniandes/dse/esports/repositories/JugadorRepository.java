package co.edu.uniandes.dse.esports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;

@Repository
public interface JugadorRepository extends JpaRepository<JugadorEntity, Long> {
}