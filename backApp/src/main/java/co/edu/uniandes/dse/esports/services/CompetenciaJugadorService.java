package co.edu.uniandes.dse.esports.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la relación entre
 * la entidad Competencia y Jugador
 */
@Slf4j
@Service

public class CompetenciaJugadorService {

	@Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private CompetenciaRepository competenciaRepository;
	
	/**
	 * Agregar un jugador a un competencia
	 *
	 * @param competenciaId  El id competencia a guardar
	 * @param jugadorId El id del jugador al cual se le va a guardar el competencia.
	 * @return El competencia que fue agregado al jugador.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public JugadorEntity addJugador(Long jugadorId, Long competenciaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociar el jugador con id = {0} al competencia con id = " + competenciaId, jugadorId);
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if (jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el jugador con el id pasado por parametro");

		Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
		if (competenciaEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro la competencia con el id pasado por parametro");

		competenciaEntity.get().setJugador(jugadorEntity.get());
		log.info("Termina proceso de asociar el jugador con id = {0} al competencia con id = {1}", jugadorId, competenciaId);
		return jugadorEntity.get();
	}

	/**
	 *
	 * Obtener un jugador por medio del id del competencia.
	 *
	 * @param competenciaId id del competencia a ser buscado.
	 * @return el jugador solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public JugadorEntity getJugador(Long competenciaId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el jugador del competencia con id = {0}", competenciaId);
		Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
		if (competenciaEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro la competencia con el id suministrado");

		JugadorEntity jugadorEntity = competenciaEntity.get().getJugador();

		if (jugadorEntity == null)
			throw new EntityNotFoundException("The player was not found");

		log.info("Termina proceso de consultar el jugador del competencia con id = {0}", competenciaId);
		return jugadorEntity;
	}

	/**
	 * Remplazar jugador de un competencia
	 *
	 * @param competenciaId  el id del competencia que se quiere actualizar.
	 * @param jugadorId El id del nuebo jugador asociado al competencia.
	 * @return el nuevo jugador asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public JugadorEntity replaceJugador(Long competenciaId, Long jugadorId) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el jugador del competencia competencia con id = {0}", competenciaId);
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if (jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el jugador");

		Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
		if (competenciaEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro la competencia");

		competenciaEntity.get().setJugador(jugadorEntity.get());
		log.info("Termina proceso de asociar el jugador con id = {0} al competencia con id = " + competenciaId, jugadorId);
		return jugadorEntity.get();
	}

	/**
	 * Borrar el jugador de un competencia
	 *
	 * @param competenciaId El competencia que se desea borrar del jugador.
	 * @throws EntityNotFoundException si el competencia no tiene jugador
	 */

	@Transactional
	public void removeJugador(Long competenciaId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el jugador del competencia con id = {0}", competenciaId);
		Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
		if (competenciaEntity.isEmpty())
			throw new EntityNotFoundException("La competencia con el id suministrado no fue encontrada");

		if (competenciaEntity.get().getJugador() == null) {
			throw new EntityNotFoundException("La competencia no tiene jugador");
		}
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(competenciaEntity.get().getJugador().getId());

		jugadorEntity.ifPresent(jugador -> {
			competenciaEntity.get().setJugador(null);
			jugador.getCompetenciasParticipadas().remove(competenciaEntity.get());
		});

		log.info("Termina proceso de borrar el jugador de la competencia con id = " + competenciaId);
	}
}
