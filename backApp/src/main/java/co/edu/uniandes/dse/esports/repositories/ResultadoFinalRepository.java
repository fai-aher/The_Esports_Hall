package co.edu.uniandes.dse.esports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;

@Repository
public interface ResultadoFinalRepository extends JpaRepository<ResultadoFinalEntity, Long> {
    
}