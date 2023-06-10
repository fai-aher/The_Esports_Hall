package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.LogrosEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;
import co.edu.uniandes.dse.esports.repositories.LogrosRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la entidad de
 * Logro.
 */
@Slf4j
@Service
public class LogrosService {
    @Autowired
    JugadorRepository jugadorRepository;

    @Autowired
    LogrosRepository logrosRepository;

    /**
	 * Se encarga de crear un Logro en la base de datos.
	 *
	 * @param logroEntity Objeto de LogrosEntity con los datos nuevos
	 * @param jugadorId       id del Jugador el cual sera padre del nuevo Logro.
	 * @return Objeto de LogrosEntity con los datos nuevos y su ID.
	 * @throws EntityNotFoundException si el jugador no existe.
	 *
	 */
    @Transactional
    public LogrosEntity createLogro(Long jugadorId, LogrosEntity logrosEntity) throws EntityNotFoundException {
        log.info("Inicia proceso de creación de un logro");
        
        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if (jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el jugador con el id suministrado.");

		logrosEntity.setJugador(jugadorEntity.get());

        log.info("Termina proceso de creación del logro.");

        return logrosRepository.save(logrosEntity);
    }

    /**
	 * Obtiene la lista de los registros de Logros que pertenecen a un Jugador.
	 *
	 * @param jugadorId id del Jugador el cual es padre de los Logros.
	 * @return Colección de objetos de LogrosEntity.
	 */

    @Transactional
    public List<LogrosEntity> getLogros(Long jugadorId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar los logros asociados al jugador con id = {0}", jugadorId);
        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
        if (jugadorEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el jugador con el id pasado por parametro");
 
        log.info("Termina proceso de consultar los logros asociados al jugador con id = {0}", jugadorId);
        return jugadorEntity.get().getLogrosObtenidos();
    }
 
     /**
      * Obtiene los datos de una instancia de Logro a partir de su ID. La existencia
      * del elemento padre Jugador se debe garantizar.
      *
      * @param jugadorId   El id del Jugador buscado
      * @param logroId Identificador del Logro a consultar
      * @return Instancia de LogrosEntity con los datos del Logro consultado.
      *
      */
    @Transactional
    public LogrosEntity getLogro(Long jugadorId, Long logroId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar el logro con id = {0} del jugador con id = " + jugadorId,
                logroId);
        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
        if (jugadorEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el jugador con ese id");
 
        Optional<LogrosEntity> logroEntity = logrosRepository.findById(logroId);
        if (logroEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el logro con el id suministrado");
 
        log.info("Termina proceso de consultar el logro con id = {0} del jugador con id = " + jugadorId,
                logroId);

        if(!jugadorEntity.get().getLogrosObtenidos().contains(logroEntity.get()))
                throw new IllegalOperationException("El logro no esta asociado al jugador");

        return logrosRepository.findByJugadorIdAndId(jugadorId, logroId);
    }
 
     /**
      * Actualiza la información de una instancia de Logro.
      *
      * @param logroEntity Instancia de LogrosEntity con los nuevos datos.
      * @param jugadorId       id del Jugador el cual sera padre del Logro actualizado.
      * @param logroId     id de la logro que será actualizada.
      * @return Instancia de LogrosEntity con los datos actualizados.
      *
      */
    @Transactional
    public LogrosEntity updateLogro(Long jugadorId, Long logroId, LogrosEntity logro) throws EntityNotFoundException {
        log.info("Inicia proceso de actualizar el logro con id = {0} del jugador con id = " + jugadorId,
                logroId);
        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
        if (jugadorEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el jugador");
 
        Optional<LogrosEntity> logroEntity = logrosRepository.findById(logroId);
        if (logroEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el logro");
 
        logro.setId(logroId);
        logro.setJugador(jugadorEntity.get());
        log.info("Termina proceso de actualizar el logro con id = {0} del jugador con id = " + jugadorId,
                logroId);
        return logrosRepository.save(logro);
    }
 
     /**
      * Elimina una instancia de Logro de la base de datos.
      *
      * @param logroId Identificador de la instancia a eliminar.
      * @param jugadorId   id del Jugador el cual es padre del Logro.
      * @throws EntityNotFoundException Si el logro no esta asociado al jugador.
     * @throws IllegalOperationException
      *
      */
    @Transactional
    public void deleteLogro(Long jugadorId, Long logroId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el logro con id = {0} del jugador con id = " + jugadorId,
                logroId);
        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
        if (jugadorEntity.isEmpty())
            throw new EntityNotFoundException("No fue encontrado el jugador");
 
        Optional<LogrosEntity> logroEntity = logrosRepository.findById(logroId);
        if (logroEntity.isEmpty())
            throw new EntityNotFoundException("No se ha encontrado el jugador con ese id.");

        logrosRepository.deleteById(logroId);
        log.info("Termina proceso de borrar el logro con id = {0} del jugador con id = " + jugadorId,
                logroId);
    }
}