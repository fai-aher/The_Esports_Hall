package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociado un equipo con respecto a sus
// Jugadores Integrantes.

/* EN ESTA ASOCIACION, JUGADOR ES LA ENTIDAD DUENA DE LA RELACION */
/* Por tanto, solo habrán métodos get. */

public class EquipoJugadorService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private JugadorRepository jugadorRepository;


    //Consultar los integrantes de un equipo
    @Transactional
    public List<JugadorEntity> getIntegrantes(Long equipoId) throws EntityNotFoundException {
            log.info("Inicia proceso de consultar todos los integrantes del equipo con id = {0}", equipoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con el id pasada por parametro");
            log.info("Finaliza proceso de consultar todos los integrantes del equipo con id = {0}", equipoId);
            return equipoEntity.get().getIntegrantes();
    }

    //Consultar un integrante particular para un equipo.
    @Transactional
    public JugadorEntity getIntegrante(Long equipoId, Long jugadorId) throws EntityNotFoundException, IllegalOperationException {
            
            log.info("Inicia proceso de consultar una competencia participada del equipo con id = {0}", equipoId);
            Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);

            if (jugadorEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el jugador integrante con esa id.");

            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con ese id.");

            log.info("Termina proceso de consultar un integrante del equipo con id = {0}", equipoId);

            if (equipoEntity.get().getIntegrantes().contains(jugadorEntity.get()))
                    return jugadorEntity.get();

            throw new IllegalOperationException("El equipo no tiene vinculado ese jugador.");
    }

    
}
