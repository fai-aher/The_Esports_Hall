package co.edu.uniandes.dse.esports.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JugadorEquipoService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private EquipoRepository equipoRepository;

     /**
	 * Remplazar el equipo de un jugador.
	 *
	 * @param jugadorId      id del jugador que se quiere actualizar.
	 * @param equipoId El id de la equipo que se será del jugador.
	 * @return el nuevo jugador.
	 */

	@Transactional
	public JugadorEntity replaceEquipo(Long jugadorId, Long equipoId) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar jugador con id = {0}", jugadorId);
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if (jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se ha encontrado el jugador con ese id.");

		Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
		if (equipoEntity.isEmpty())
			throw new EntityNotFoundException("No se ha encontrado el equipo con ese id.");

		jugadorEntity.get().setEquipo(equipoEntity.get());
		log.info("Termina proceso de actualizar libro con id = {0}", jugadorId);

		return jugadorEntity.get();
	}

	/**
	 * Borrar un jugador de un equipo. Este metodo se utiliza para borrar la
	 * relacion de un jugador.
	 *
	 * @param jugadorId El jugador que se desea borrar de la equipo.
	 */
	@Transactional
	public void removeEquipo(Long jugadorId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el Equipo del jugador con id = {0}", jugadorId);
		Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
		if (jugadorEntity.isEmpty())
			throw new EntityNotFoundException("No se ha encontrado el jugador con ese id.");

		Optional<EquipoEntity> equipoEntity = equipoRepository
				.findById(jugadorEntity.get().getEquipo().getId());
		equipoEntity.ifPresent(equipo -> equipo.getIntegrantes().remove(jugadorEntity.get()));

		jugadorEntity.get().setEquipo(null);
		log.info("Termina proceso de borrar el Equipo del jugador con id = {0}", jugadorId);
	}    
}