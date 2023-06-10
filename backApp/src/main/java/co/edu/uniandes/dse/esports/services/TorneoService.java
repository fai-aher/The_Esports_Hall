package co.edu.uniandes.dse.esports.services;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;

import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.repositories.TorneoRepository;

@Slf4j
@Service
public class TorneoService {

	@Autowired
	TorneoRepository torneoRepository;
    
    /**
	 * Guardar un Torneo
	 *
	 * @param torneoEntity La entidad de tipo Torneo del nuevo Torneo a persistir.
	 * @return La entidad luego de persistirla
	 * @throws IllegalOperationException Si la lista de competencias y/o la de
	 * 									 equipos es nula, y si la fecha de realización
	 * 									 no es anterior a la actual
	 */

    @Transactional
    public TorneoEntity createTorneo(TorneoEntity torneoEntity) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de creacion del Torneo");
		
		if (torneoEntity.getCompetencias() == null)
			throw new IllegalOperationException("La lista de competencias no es válida (es nula)");

		if (torneoEntity.getEquiposParticipantes() == null)
			throw new IllegalOperationException("La lista de equipos participantes no es válida (es nula)");

		Date fechaActual = new Date();

		if (!(torneoEntity.getFechaFinalizacion().before(fechaActual)))
			throw new IllegalOperationException("La fecha de finalizacion es inválida");

        log.info("Termina proceso de creacion del Torneo");
        return torneoRepository.save(torneoEntity);
    }

    /**
	 * Devuelve todos los Torneos que hay en la base de datos.
	 *
	 * @return Lista de entidades de tipo Torneo.
	 */

	@Transactional
	public List<TorneoEntity> getTorneos() {

		log.info("Inicia proceso de consultar todos los Torneos");
		return torneoRepository.findAll();
	}

    /**
	 * Busca un Torneo por ID
	 *
	 * @param torneoId El id del Torneo a buscar
	 * @return El Torneo encontrado
	 * @throws EntityNotFoundException Si el Torneo no se encuentra
	 */
     
	@Transactional
	public TorneoEntity getTorneo(Long torneoId) throws EntityNotFoundException {

		log.info("Inicia proceso de consultar el Torneo con id = {0}", torneoId);

		Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
		if (torneoEntity.isEmpty())
			throw new EntityNotFoundException("No se ha encontrado el torneo con el id dado");

		log.info("Termina proceso de consultar el Torneo con id = {0}", torneoId);
		return torneoEntity.get();
	}

	/**
	 * Actualizar un Torneo por ID
	 *
	 * @param torneoId    El ID del torneo a actualizar
	 * @param torneo La entidad del torneo con los cambios deseados
	 * @return La entidad del torneo luego de actualizarla
	 * @throws IllegalOperationException Si la lista de competencias y/o la de
	 * 									 equipos es nula, y si la fecha de realización
	 * 									 no es anterior a la actual
	 * @throws EntityNotFoundException Si torneo no es encontrado
	 */

	@Transactional
	public TorneoEntity updateTorneo(Long torneoId, TorneoEntity torneo)
			throws EntityNotFoundException, IllegalOperationException {

		log.info("Inicia proceso de actualizar el torneo con id = {0}", torneoId);

		Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
		if (torneoEntity.isEmpty())
			throw new EntityNotFoundException("No se ha encontrado el torneo con el id pasado por parametro");
		
		if (torneo.getCompetencias() == null)
			throw new IllegalOperationException("La lista de competencias no es válida (es nula)");

		if (torneo.getEquiposParticipantes() == null)
			throw new IllegalOperationException("La lista de equipos participantes no es válida (es nula)");

		Date fechaActual = new Date();

		if (!(torneo.getFechaFinalizacion().before(fechaActual)))
			throw new IllegalOperationException("La fecha de finalizacion es inválida");

		torneo.setId(torneoId);

		log.info("Termina proceso de actualizar el torneo con id = {0}",torneoId);		
		return torneoRepository.save(torneo);
	}

	/**
	 * Eliminar un torneo por ID
	 *
	 * @param torneoId El ID del torneo a eliminar
	 * @throws IllegalOperationException si ...
	 * @throws EntityNotFoundException si el torneo no existe
	 */
	@Transactional
	public void deleteTorneo(Long torneoId) throws EntityNotFoundException, IllegalOperationException {

		log.info("Inicia proceso de borrar el torneo con id = {0}", torneoId);
		
		Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
		if (torneoEntity.isEmpty())
			throw new EntityNotFoundException("No se ha encontrado el torneo con el id dado.");

		torneoRepository.deleteById(torneoId);
		log.info("Termina proceso de borrar el torneo con id = {0}", torneoId);
	}
}
