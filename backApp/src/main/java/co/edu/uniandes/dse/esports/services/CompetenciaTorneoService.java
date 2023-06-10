package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.repositories.TorneoRepository;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociada una competencia con respecto a sus
// torneo

/* EN ESTA ASOCIACION, COMPETENCIA ES LA ENTIDAD DUENA DE LA RELACION */
public class CompetenciaTorneoService {
    
    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

    //Adicionar una competencia a un torneo
    @Transactional
    public TorneoEntity addTorneoCompetencia(Long competenciaId, Long torneoId) throws EntityNotFoundException {
            log.info("Inicia proceso de asociarle una competencia al torneo con id = {0}", torneoId);
            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado al competencia con el id pasado por parametro.");

            Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
            if (torneoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el torneo con el id pasado por parametro");

            competenciaEntity.get().setTorneo(torneoEntity.get());
            log.info("Termina proceso de asociarle un torneo a una competencia con id = {0}", competenciaId);
            return torneoEntity.get();
    }

    //Consultar el torneo de una competencia
    @Transactional
    public TorneoEntity getTorneoCompetencia(Long competenciaId) throws EntityNotFoundException 
    {
            log.info("Inicia proceso de consultar el torneo de la competencia con id = {0}", competenciaId);
            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado una competencia con el id pasada por parametro");
            log.info("Finaliza proceso de consultar el torneo de la competencia con id = {0}", competenciaId);
            return competenciaEntity.get().getTorneo();
    }

    //Quitar una competencia de un torneo
    @Transactional
    public void removeTorneoCompetencia(Long torneoId, Long competenciaId) throws EntityNotFoundException {
            log.info("Inicia proceso de borrar una competencia de un torneo = {0}", competenciaId);
            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
            Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);

            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado la competencia participada con ese id.");

            if (torneoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado un torneo con ese id.");

            torneoEntity.get().getCompetencias().remove(competenciaEntity.get());

            log.info("Termina proceso de borrar del torneo una competencia con id = {0}", competenciaId);
    }
}