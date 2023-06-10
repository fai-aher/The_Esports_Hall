package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociado un equipo con respecto a sus
// Competencias Ganadas

/* EN ESTA ASOCIACION, COMPETENCIA ES LA ENTIDAD DUENA DE LA RELACION */
/* Por tanto, solo habran metodos get. */
public class EquipoCompetenciasGanadasService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;


    //Consultar los integrantes de un equipo
    @Transactional
    public List<CompetenciaEntity> getCompetenciasGanadas(Long equipoId) throws EntityNotFoundException {
            log.info("Inicia proceso de consultar las competencias ganadas del equipo con id = {0}", equipoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con el id pasada por parametro");
            log.info("Finaliza proceso de consultar las competencias ganadas del equipo con id = {0}", equipoId);

            return equipoEntity.get().getCompetenciasGanadas();
    }


    //Consultar un integrante particular para un equipo.
    @Transactional
    public CompetenciaEntity getCompetenciaGanada(Long equipoId, Long competenciaId) throws EntityNotFoundException, IllegalOperationException {
            
            log.info("Inicia proceso de consultar una competencia ganada especifica del equipo con id = {0}", equipoId);

            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);

            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado una competencia ganada con esa id.");

            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con ese id.");

            log.info("Termina proceso de consultar una competencia ganada especifica del equipo con id = {0}", equipoId);

            if (equipoEntity.get().getCompetenciasGanadas().contains(competenciaEntity.get()))
                    return competenciaEntity.get();

            throw new IllegalOperationException("El equipo no parece haber ganado esa competencia.");
    }


    
}
