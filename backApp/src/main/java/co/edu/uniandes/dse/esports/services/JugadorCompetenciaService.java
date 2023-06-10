package co.edu.uniandes.dse.esports.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con la persistencia para la relación entre
 * la entidad Jugador y Competencia.
 */
@Slf4j
@Service
public class JugadorCompetenciaService {
    @Autowired
	private CompetenciaRepository competenciaRepository;

	@Autowired
	private JugadorRepository jugadorRepository;
	
	/**
	 * Agregar una competencia al jugador
	 *
	 * @param competenciaId      El id competencia a guardar
	 * @param jugadorId El id del jugador en la cual se va a guardar la competencia.
	 * @return La competencia creada.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public CompetenciaEntity addCompetencia(Long competenciaId, Long jugadorId) throws EntityNotFoundException {
		log.info("Inicia proceso de agregarle una competencia al jugador con id = {0}", jugadorId);
		
		Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
		if(competenciaEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro la competencia con el id suminsitrado");
		
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if(jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el jugador con el id suministrado");
		
		competenciaEntity.get().setJugador(jugadorEntity.get());
		log.info("Termina proceso de agregarle una competencia al jugador con id = {0}", jugadorId);
		return competenciaEntity.get();
	}

	/**
	 * Retorna todos los jugadores asociados a una competencia
	 *
	 * @param jugadorId El ID del jugador buscado
	 * @return La lista de competencias del jugador
	 * @throws EntityNotFoundException si el jugador no existe
	 */
	@Transactional
	public List<CompetenciaEntity> getCompetencias(Long jugadorId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar las competencias asociadas al jugador con id = {0}", jugadorId);
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if(jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el jugador con el id pasado por parametro");
		
		return jugadorEntity.get().getCompetenciasParticipadas();
	}

	/**
	 * Retorna una competencia asociada a un jugador
	 *
	 * @param jugadorId El id del jugador a buscar.
	 * @param competenciaId      El id de la competencia a buscar
	 * @return El competencia encontrado dentro del jugador.
	 * @throws EntityNotFoundException Si la competencia no se encuentra en el jugador
	 * @throws IllegalOperationException Si la competnecia no está asociado a el jugador
	 */
	@Transactional
	public CompetenciaEntity getCompetencia(Long jugadorId, Long competenciaId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar la competencia con id = {0} del jugador con id = " + jugadorId, competenciaId);
		
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if(jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encotnro el jugador con el id suministrado");
		
		Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
		if(competenciaEntity.isEmpty())
			throw new EntityNotFoundException("No se encotnro la competencia con el id suministrado");
				
		log.info("Termina proceso de consultar la competencia con id = {0} del jugador con id = " + jugadorId, competenciaId);
		
		if(!jugadorEntity.get().getCompetenciasParticipadas().contains(competenciaEntity.get()))
			throw new IllegalOperationException("La competencia no esta asociada al jugador");
		
		return competenciaEntity.get();
	}

	/**
	 * Remplazar competencias de un jugador
	 *
	 * @param competencias        Lista de competencias que serán los del jugador.
	 * @param jugadorId El id del jugador que se quiere actualizar.
	 * @return La lista de competencias actualizada.
	 * @throws EntityNotFoundException Si el jugador o una competencia de la lista no se encuentran
	 */
	@Transactional
	public List<CompetenciaEntity> replaceCompetencias(Long jugadorId, List<CompetenciaEntity> competencias) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el jugador con id = {0}", jugadorId);
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if(jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro el jugador con el id suministrado.");
		
		for(CompetenciaEntity competencia : competencias) {
			Optional<CompetenciaEntity> c = competenciaRepository.findById(competencia.getId());
			if(c.isEmpty())
				throw new EntityNotFoundException("No se encontro la competencia");
			
			c.get().setJugador(jugadorEntity.get());
		}		
		return competencias;
	}
}

