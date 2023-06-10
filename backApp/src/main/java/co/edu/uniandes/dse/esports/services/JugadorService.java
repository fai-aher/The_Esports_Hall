package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import java.util.Date;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;
import co.edu.uniandes.dse.esports.repositories.LogrosRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JugadorService {
    @Autowired
    JugadorRepository jugadorRepository;

    @Autowired
    LogrosRepository logroRepository;

    @Transactional
    public JugadorEntity createJugador(JugadorEntity jugadorEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de un jugador");
                
        if (jugadorEntity.getEquipo() == null)
            throw new IllegalOperationException("El jugador debe pertenecer a un equipo.");

        Optional<JugadorEntity> alreadyExists = jugadorRepository.findById(jugadorEntity.getId());
        if (!alreadyExists.isEmpty())
            throw new IllegalOperationException("Ya existe el jugador que se quiere crear.");
        
        Date fechaActual = new Date();

		if (!(jugadorEntity.getFechaNacimiento().before(fechaActual)))
			throw new IllegalOperationException("La fecha de nacimiento es inválido");

        log.info("Termina proceso de creación del jugador.");

        return jugadorRepository.save(jugadorEntity);
        }

    @Transactional
    public List<JugadorEntity> getAllJugadores() {
    
        log.info("Comienza el proceso de buscar todos los jugadores del repositorio.");
        return jugadorRepository.findAll();
    }

    @Transactional
    public JugadorEntity getJugador(Long jugadorID) throws EntityNotFoundException {
        
        log.info("Comienza el proceso para buscar un jugador con la id = {0}", jugadorID);

        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorID);
        if (jugadorEntity.isEmpty())
                throw new EntityNotFoundException("No se ha encontrado un jugador con el ID suministrado.");

        log.info("Finaliza el proceso para buscar un jugador con la id = {0}", jugadorID);
        return jugadorEntity.get();
    }

    @Transactional
    public JugadorEntity updateJugador(Long jugadorID, JugadorEntity nuevaVerJugador) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia el proceso de actualizar el jugador con id = {0}", jugadorID);

        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorID);
        if (jugadorEntity.isEmpty())
                throw new EntityNotFoundException("No se encontro un jugador con ese Identificador.");
                
        if (nuevaVerJugador.getEquipo() == null)
            throw new IllegalOperationException("No se puede actualizar porque el jugador debe tener un equipo asociado");

        nuevaVerJugador.setId(jugadorID);
        log.info("Termina el proceso de actualizar el jugador con id = {0}", jugadorID);

        return jugadorRepository.save(nuevaVerJugador);
    }

    @Transactional
    public void deleteJugador(Long jugadorID) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia el proceso para borrar el jugador con id = {0}", jugadorID);

        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorID);
        if (jugadorEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro un jugador con el Identificador proporcionado.");

        jugadorRepository.deleteById(jugadorID);
        log.info("Termina el proceso para borrar el jugador con id = {0}", jugadorID);

    }
}

