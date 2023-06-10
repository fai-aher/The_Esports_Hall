package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociada una competencia con respecto a sus
// equipos participantes

/* EN ESTA ASOCIACION, EQUIPO ES LA ENTIDAD DUENA DE LA RELACION */
public class CompetenciaEquipoService {
    
    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

    //Consultar los equipos participantes de una competencia
    @Transactional
    public List<EquipoEntity> getEquiposParticipantes(Long competenciaId) throws EntityNotFoundException 
    {
        log.info("Inicia proceso de consultar los equipos participantes de la competencia con id = {0}", competenciaId);
        Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
        if (competenciaEntity.isEmpty())
                throw new EntityNotFoundException("No se ha encontrado una competencia con el id proporcionado");
        log.info("Finaliza proceso de consultar todos los equipos participantes de la competencia con id = {0}", competenciaId);
        return competenciaEntity.get().getEquiposParticipantes();
    }

    //Consultar un equipo participante de una competencia
    @Transactional
    public EquipoEntity getEquipoParticipante(Long competenciaId, Long equipoId) throws EntityNotFoundException, IllegalOperationException 
    {
            
            log.info("Inicia proceso de consultar un equipo participante de una competencia con id = {0}", equipoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);

            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado la competencia con el id dado.");

            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con ese id.");

            log.info("Termina proceso de consultar un equipo participante con el id = {0}", equipoId);

            if (competenciaEntity.get().getEquiposParticipantes().contains(equipoEntity.get()))
                    return equipoEntity.get();

            throw new IllegalOperationException("El equipo no es participante de ese torneo");
    }
}
