package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.TorneoEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;
import co.edu.uniandes.dse.esports.repositories.TorneoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociado un equipo con respecto a sus
// Torneos participados.

/* EN ESTA ASOCIACION, EQUIPO ES LA ENTIDAD DUENA DE LA RELACION */

public class EquipoTorneoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    //Adicionar un torneo participado a un equipo.
    @Transactional
    public TorneoEntity addTorneoParticipado(Long equipoId, Long torneoId) throws EntityNotFoundException {
            log.info("Inicia proceso de asociarle un torneo al equipo con id = {0}", equipoId);
            Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
            if (torneoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el torneo con el id pasado por parametro.");

            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con el id pasado por parametro");

            equipoEntity.get().getTorneosParticipados().add(torneoEntity.get());
            log.info("Termina proceso de asociarle un torneo participado al equipo con id = {0}", equipoId);
            return torneoEntity.get();

    }

    //Consultar los torneos participados de un equipo
    @Transactional
    public List<TorneoEntity> getTorneosParticipados(Long equipoId) throws EntityNotFoundException {
            log.info("Inicia proceso de consultar todos los torneos participados del equipo con id = {0}", equipoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con el id pasada por parametro");

            log.info("Finaliza proceso de consultar todos los torneos participados del equipo con id = {0}", equipoId);
            return equipoEntity.get().getTorneosParticipados();
    }

    //Consultar un torneo participado particular para un equipo.
    @Transactional
    public TorneoEntity getTorneoParticipado(Long equipoId, Long torneoId) throws EntityNotFoundException, IllegalOperationException {
            
            log.info("Inicia proceso de consultar un torneo participado del equipo con id = {0}", equipoId);
            Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);

            if (torneoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el torneo participado con ese id.");

            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con ese id.");

            log.info("Termina proceso de consultar un torneo participado del equipo con id = {0}", equipoId);

            if (equipoEntity.get().getTorneosParticipados().contains(torneoEntity.get()))
                    return torneoEntity.get();

            throw new IllegalOperationException("El equipo no ha participado en ese torneo.");
    }


    //Actualizar o reemplazar todos los torneos en las que ha participado un equipo.
    @Transactional
    public List<TorneoEntity> replaceTorneosParticipados(Long equipoId, List<TorneoEntity> list) throws EntityNotFoundException {
            log.info("Inicia proceso de reemplazar los torneos participados del equipo con id = {0}", equipoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con el id suministrado.");

            List<TorneoEntity> torneosVacios = new ArrayList<>();

            equipoEntity.get().setTorneosParticipados(torneosVacios);

            for (TorneoEntity torneo : list) {
                    Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneo.getId());
                    if (torneoEntity.isEmpty())
                            throw new EntityNotFoundException("No se ha encontrado un torneo de la nueva lista.");

                    if (!equipoEntity.get().getTorneosParticipados().contains(torneoEntity.get()))
                            equipoEntity.get().getTorneosParticipados().add(torneoEntity.get());
            }
            log.info("Termina proceso de reemplazar los torneos participados del equipo con id = {0}", equipoId);
            return getTorneosParticipados(equipoId);
    }

    //Quitar un torneo participado de un cierto equipo
    @Transactional
    public void removeTorneoParticipado(Long equipoId, Long torneoId) throws EntityNotFoundException {
            log.info("Inicia proceso de borrar un torneo participado del equipo con id = {0}", equipoId);
            Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);

            if (torneoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el torneo participado con ese id.");

            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con el id pasado por parametro.");

            equipoEntity.get().getTorneosParticipados().remove(torneoEntity.get());

            log.info("Termina proceso de borrar un torneo participado del equipo con id = {0}", equipoId);
    }
    
}
