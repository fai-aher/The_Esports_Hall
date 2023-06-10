package co.edu.uniandes.dse.esports.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;

@Repository
public interface EquipoRepository extends JpaRepository<EquipoEntity, Long> {
    
}
