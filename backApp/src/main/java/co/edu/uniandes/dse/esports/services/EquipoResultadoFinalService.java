package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociado un equipo con respecto a sus
// Resultados finales obtenidos.

/* EN ESTA ASOCIACION, RESULTADOFINAL ES LA ENTIDAD DUENA DE LA RELACION */
/* Por tanto, solo habran metodos get. */
public class EquipoResultadoFinalService {

    
    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;


    //Consultar los integrantes de un equipo
    @Transactional
    public List<ResultadoFinalEntity> getResultadoFinalEntities(Long equipoId) throws EntityNotFoundException {
            log.info("Inicia proceso de consultar todos los resultados finales del equipo con id = {0}", equipoId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con el id pasada por parametro");
            log.info("Finaliza proceso de consultar todos los resultados finales del equipo con id = {0}", equipoId);
            return equipoEntity.get().getResultadosEnCompetencias();
    }

    //Consultar un integrante particular para un equipo.
    @Transactional
    public ResultadoFinalEntity getResultadoDeUnaCompetencia(Long equipoId, Long competenciaId) throws EntityNotFoundException, IllegalOperationException {
            
            log.info("Inicia proceso de consultar un resultado final en una competencia para el equipo con id = {0}", equipoId);
            Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
            Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);

            if (competenciaEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado una competencia con esa id.");

            if (equipoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el equipo con ese id.");

            log.info("Termina proceso de consultar un resultado final en una competencia para el equipo con id = {0}", equipoId);

            for (ResultadoFinalEntity resultadoFinal : equipoEntity.get().getResultadosEnCompetencias())
                if (resultadoFinal.getCompetenciaRelacionada().getId().equals(competenciaId)) {
                                return resultadoFinal;
                }
        
                //En caso que no exista el resultado para la competencia y equipo especificados.
            throw new IllegalOperationException("El equipo no tiene un resultado final vinculado a la competencia dada por esa id.");
    }
    
}
