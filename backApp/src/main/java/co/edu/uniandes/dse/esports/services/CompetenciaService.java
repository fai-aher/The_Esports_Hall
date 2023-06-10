
package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompetenciaService
{
    @Autowired
    JugadorRepository jugadorRepository;

    @Autowired
    CompetenciaRepository competenciaRepository;

    /**
	 * Se encarga de crear una Competencia en la base de datos.
	 *
	 * @param competenciaEntity Objeto de CompetenciaEntity con los datos nuevos
	 * @param jugadorId       id del Jugador el cual sera padre del nuevo Competencia.
	 * @return Objeto de CompetenciaEntity con los datos nuevos y su ID.
	 * @throws EntityNotFoundException si el jugador no existe.
	 *
	 */
    @Transactional
    public CompetenciaEntity createCompetencia(CompetenciaEntity competenciaEntity) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia la creacion de una competencia");
        
        if (competenciaEntity.getDuracion() == null)
        {
            throw new IllegalOperationException("La duracion de la competencia no es valida (nula)");
        }
        if (competenciaEntity.getEquiposParticipantes()== null)
        {
            throw new IllegalOperationException("La lista de equipos participantes no es valida (nula)");
        }

        log.info("Termina la creacion de una competencia");
        return competenciaRepository.save(competenciaEntity);
    }

    /**
	 * Obtiene la lista de los registros de Competencias.
	 *
	 * @return Colección de objetos de CompetenciasEntity.
	 */
    @Transactional
    public List<CompetenciaEntity> getCompetencias()
    {
        log.info("Inicia proceso de consultar todas las competencias");
        return competenciaRepository.findAll();   
    }

    /**
	 * Obtiene la lista de los registros de CompetenciasMvp que pertenecen a un Jugador.
	 *
	 * @param jugadorId id del Jugador el cual es padre de las Competencias.
	 * @return Colección de objetos de CompetenciasEntity.
	 */
    @Transactional
    public List<CompetenciaEntity> getCompetenciasMvp(Long jugadorId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar las competencias mvp asociados al jugador con id = {0}", jugadorId);
        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
        if (jugadorEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el jugador con el id pasado por parametro");
 
        log.info("Termina proceso de consultar los competencias asociados al jugador con id = {0}", jugadorId);
        return jugadorEntity.get().getCompetenciasMVP();
    }

    /**
      * Obtiene los datos de una instancia de Competenci a partir de su ID.
      *
      * @param competenciaId Identificador de la Competencia a consultar
      * @return Instancia de CompetenciaEntity con los datos de la competencia consultada.
      *
      */
    @Transactional
    public CompetenciaEntity getCompetencia(Long competenciaID) throws EntityNotFoundException
    {
        log.info("Comienza el proceso para buscar una competencia con id = {0}", competenciaID);

        Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaID);

        if (competenciaEntity.isEmpty())
                throw new EntityNotFoundException("No se ha encontrado una competencia con el ID suministrado.");
        
        log.info("Finaliza el proceso para buscar una competencia con id = {0}", competenciaID);

        return competenciaEntity.get();
    }

    /**
      * Obtiene los datos de una instancia de competencia a partir de su ID. La existencia
      * del elemento padre Jugador se debe garantizar.
      *
      * @param jugadorId   El id del Jugador buscado
      * @param competenciaId Identificador del competencia a consultar
      * @return Instancia de CompetenciaEntity con los datos del competencia consultado.
      *
      */
    @Transactional
    public CompetenciaEntity getCompetenciaMvp(Long jugadorId, Long competenciaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el competencia con id = {0} del jugador con id = " + jugadorId,
                competenciaId);
        Optional<JugadorEntity> jugadorEntity = jugadorRepository.findById(jugadorId);
        if (jugadorEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el jugador con ese id");
   
        Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
        if (competenciaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el competencia con el id suministrado");
   
        log.info("Termina proceso de consultar el competencia con id = {0} del jugador con id = " + jugadorId,
                competenciaId);
        return competenciaRepository.findByJugadorIdAndId(jugadorId, competenciaId);
    }

    /**
      * Actualiza la información de una instancia de Competencia.
      *
      * @param nuevaVerCompe Instancia de CompetenciasEntity con los nuevos datos.
      * @param jugadorId id del Jugador el cual sera padre del Logro actualizado.
      * @param competenciaID id de la competencia que será actualizada.
      * @return Instancia de CompetenciasEntity con los datos actualizados.
      *
      */
    @Transactional
    public CompetenciaEntity updateCompetencia(Long competenciaID, CompetenciaEntity nuevaVerCompe) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia el proceso de actualizar la competencia con id = {0}", competenciaID);

        Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaID);
		if (competenciaEntity.isEmpty())
        {
			throw new EntityNotFoundException("No se ha encontrado una competencia con el id dado");
        }
        if (nuevaVerCompe.getEquiposParticipantes() == null)
        {
			throw new IllegalOperationException("La lista de equipos participantes no es válida (es nula)");
        }
        if (nuevaVerCompe.getDuracion() == null)
        {
            throw new IllegalOperationException("La duracion de la competencia no es valida (nula)");
        }

		nuevaVerCompe.setId(competenciaID);
		log.info("Termina proceso de actualizar el torneo con id = {0}",competenciaID);		
		return competenciaRepository.save(nuevaVerCompe);
    }

    /**
      * Elimina una instancia de Competencia de la base de datos.
      *
      * @param competenciaID Identificador de la instancia a eliminar.
      * @throws EntityNotFoundException Si la competencia no fue encontrada.
      *
      */
    @Transactional
    public void deleteCompetencia(Long competenciaID) throws EntityNotFoundException {

        log.info("Inicia proceso de borrar una competencia con id = {0}", competenciaID);

        Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaID);

        if (competenciaEntity.isEmpty())
                throw new EntityNotFoundException("No se encontro una competencia con el Identificador proporcionado.");

        competenciaRepository.deleteById(competenciaID);
        log.info("Termina el proceso para borrar la competencia con id = {0}", competenciaID);
    }
}