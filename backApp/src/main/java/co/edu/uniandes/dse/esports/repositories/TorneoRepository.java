package co.edu.uniandes.dse.esports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.esports.entities.TorneoEntity;

@Repository
public interface TorneoRepository extends JpaRepository<TorneoEntity, Long> {
        
}