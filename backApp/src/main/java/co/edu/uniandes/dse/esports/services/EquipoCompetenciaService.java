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
// Competencias Participadas

/* EN ESTA ASOCIACION, EQUIPO ES LA ENTIDAD DUENA DE LA RELACION */
public class EquipoCompetenciaService {
    
    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

    //Adicionar una competencia participada a un equipo.
    @Transactional
    public CompetenciaEntity addCompetenciaParticipada(Long equipoId, Long competenciaId) throws EntityNotFoundException, IllegalOperationException {
            log.info("Inicia proceso de asociarle una competencia al equipo con id = {0}", equipoId);

            if (competenciaId == null)
                throw new IllegalOperationException("No se acepta adicionar una competencia con ID null.");

            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
            
            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado la competencia con el id pasado por parametro.");

            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con el id");

            //Si la competencia ya fue agregada al mismo equipo.

            List<CompetenciaEntity> competenciasParticipadas = equipoEntity.get().getCompetenciasParticipadas();
        
            for (int i = 0; i < competenciasParticipadas.size(); i++) {

                if (competenciasParticipadas.get(i).getId().equals(competenciaEntity.get().getId()))
                        throw new IllegalOperationException("El equipo ya tiene asignado esa competencia participada.");

            }

            equipoEntity.get().getCompetenciasParticipadas().add(competenciaEntity.get());
            log.info("Termina proceso de asociarle una competencia participada al equipo con id = {0}", equipoId);
            return competenciaEntity.get();

    }

    //Consultar las competencias participadas de un equipo
    @Transactional
    public List<CompetenciaEntity> getCompetenciasParticipadas(Long equipoId) throws EntityNotFoundException {
            log.info("Inicia proceso de consultar todas las competencias participadas del equipo con id = {0}", equipoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con la id pasada por parametro.");
            log.info("Finaliza proceso de consultar todas las competencias participadas del equipo con id = {0}", equipoId);
            return equipoEntity.get().getCompetenciasParticipadas();
    }

    //Consultar una competencia participada particular para un equipo.
    @Transactional
    public CompetenciaEntity getCompetenciaParticipada(Long equipoId, Long competenciaId) throws EntityNotFoundException, IllegalOperationException {
            
            log.info("Inicia proceso de consultar una competencia participada del equipo con id = {0}", equipoId);
            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);

            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado la competencia participada con esa id.");

            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con ese id pasado por parámetro.");
            log.info("Termina proceso de consultar una competencia participada del equipo con id = {0}", equipoId);
            if (equipoEntity.get().getCompetenciasParticipadas().contains(competenciaEntity.get()))
                    return competenciaEntity.get();

            throw new IllegalOperationException("El equipo no ha participado en esa competencia.");
    }

    //Actualizar o reemplazar todas las competencias en las que ha participado un equipo.
    @Transactional
    public List<CompetenciaEntity> replaceCompetenciasParticipadas(Long equipoId, List<CompetenciaEntity> list) throws EntityNotFoundException, IllegalOperationException {
            log.info("Inicia proceso de reemplazar las competencias participadas del equipo con id = {0}", equipoId);

            if (list == null)
                throw new IllegalOperationException("No se acepta adicionar una lista de competencias null.");

            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con ese identificador.");

            for (CompetenciaEntity competencia : list) {
                    Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competencia.getId());
                    if (competenciaEntity.isEmpty())
                            throw new EntityNotFoundException("No se ha encontrado una competencia de la nueva lista.");

                    if (!equipoEntity.get().getCompetenciasParticipadas().contains(competenciaEntity.get()))
                            equipoEntity.get().getCompetenciasParticipadas().add(competenciaEntity.get());
            }
            log.info("Termina proceso de reemplazar las competencias participadas del equipo con id = {0}", equipoId);
            return getCompetenciasParticipadas(equipoId);
    }

    //Quitar una competencia participada de un cierto equipo
    @Transactional
    public void removeCompetenciaParticipada(Long equipoId, Long competenciaId) throws EntityNotFoundException {
            log.info("Inicia proceso de borrar una competencia participada del equipo con id = {0}", equipoId);
            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);

            if (equipoEntity.isEmpty())
                throw new EntityNotFoundException("No se ha encontrado el equipo con ese id.");

            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado la competencia participada con ese id.");

            equipoEntity.get().getCompetenciasParticipadas().remove(competenciaEntity.get());

            log.info("Termina proceso de borrar una competencia participada del equipo con id = {0}", equipoId);
    }
}

