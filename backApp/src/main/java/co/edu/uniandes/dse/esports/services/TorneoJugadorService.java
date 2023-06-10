package co.edu.uniandes.dse.esports.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;
import co.edu.uniandes.dse.esports.repositories.TorneoRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la relación entre
 * la entidad Torneo y Jugador
 */
@Slf4j
@Service

public class TorneoJugadorService {
    @Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private TorneoRepository torneoRepository;
	
	/**
	 * Agregar un jugador a un torneo
	 *
	 * @param torneoId  El id torneo a guardar
	 * @param jugadorId El id del jugador al cual se le va a guardar el torneo.
	 * @return El torneo que fue agregado al jugador.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public JugadorEntity addJugador(Long jugadorId, Long torneoId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociar el jugador con id = {0} al torneo con id = " + torneoId, jugadorId);
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if (jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el jugador con el id pasado por parametro");

		Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
		if (torneoEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el torneo con el id pasado por parametro");

		torneoEntity.get().setJugador(jugadorEntity.get());
		log.info("Termina proceso de asociar el jugador con id = {0} al torneo con id = {1}", jugadorId, torneoId);
		return jugadorEntity.get();
	}

	/**
	 *
	 * Obtener un jugador por medio del id del torneo.
	 *
	 * @param torneoId id del torneo a ser buscado.
	 * @return el jugador solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public JugadorEntity getJugador(Long torneoId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el jugador del torneo con id = {0}", torneoId);
		Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
		if (torneoEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el torneo con el id suministrado");

		JugadorEntity jugadorEntity = torneoEntity.get().getJugador();

		if (jugadorEntity == null)
			throw new EntityNotFoundException("The player was not found");

		log.info("Termina proceso de consultar el jugador del torneo con id = {0}", torneoId);
		return jugadorEntity;
	}

	/**
	 * Remplazar jugador de un torneo
	 *
	 * @param torneoId  el id del torneo que se quiere actualizar.
	 * @param jugadorId El id del nuebo jugador asociado al torneo.
	 * @return el nuevo jugador asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public JugadorEntity replaceJugador(Long torneoId, Long jugadorId) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el jugador del torneo torneo con id = {0}", torneoId);
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if (jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el jugador");

		Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
		if (torneoEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro la torneo");

		torneoEntity.get().setJugador(jugadorEntity.get());
		log.info("Termina proceso de asociar el jugador con id = {0} al torneo con id = " + torneoId, jugadorId);
		return jugadorEntity.get();
	}

	/**
	 * Borrar el jugador de un torneo
	 *
	 * @param torneoId El torneo que se desea borrar del jugador.
	 * @throws EntityNotFoundException si el torneo no tiene jugador
	 */

	@Transactional
	public void removeJugador(Long torneoId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el jugador del torneo con id = {0}", torneoId);
		Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
		if (torneoEntity.isEmpty())
			throw new EntityNotFoundException("La torneo con el id suministrado no fue encontrada");

		if (torneoEntity.get().getJugador() == null) {
			throw new EntityNotFoundException("La torneo no tiene jugador");
		}
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(torneoEntity.get().getJugador().getId());

		jugadorEntity.ifPresent(jugador -> {
			torneoEntity.get().setJugador(null);
			jugador.getTorneosParticipados().remove(torneoEntity.get());
		});

		log.info("Termina proceso de borrar el jugador del torneo con id = " + torneoId);
	}
}
