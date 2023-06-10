package co.edu.uniandes.dse.esports.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.ResultadoFinalRepository;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociado un equipo con respecto a sus
// Competencias Participadas

/* EN ESTA ASOCIACION, RESULTADO FINAL ES LA ENTIDAD DUENA DE LA RELACION */

public class ResultadoFinalCompetenciaService {

    @Autowired
    private ResultadoFinalRepository resultadoFinalRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

        //Adicionar una competencia relacionada a un ResultadoFinal.
        @Transactional
        public CompetenciaEntity addCompetenciaAsociada(Long resultadoFinalId, Long competenciaId) throws EntityNotFoundException, IllegalOperationException {
                log.info("Inicia proceso de asociarle una competencia al resultado con id = {0}", resultadoFinalId);

                if (competenciaId == null)
                        throw new IllegalOperationException("La id de la competencia pasada por parametro no puede ser null.");

                Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
                if (competenciaEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado la competencia con el id pasado por parametro.");
    
                Optional<ResultadoFinalEntity> resultadoEntity = resultadoFinalRepository.findById(resultadoFinalId);
                if (resultadoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el resultado con el id");
    
                resultadoEntity.get().setCompetenciaRelacionada(competenciaEntity.get());
                log.info("Termina proceso de asociarle una competencia ascoiada al resultado con id = {0}", resultadoFinalId);
                return competenciaEntity.get();
    
        }
    
        //Consultar la competencia relacionada con un resultado
        @Transactional
        public CompetenciaEntity getCompetenciaAsociada(Long resultadoId) throws EntityNotFoundException {
                log.info("Inicia proceso de consultar todas la competencia asociada con el resultado con id = {0}", resultadoId);
                Optional<ResultadoFinalEntity> resultadoEntity = resultadoFinalRepository.findById(resultadoId);
                if (resultadoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el resultado con la id pasada por parametro.");
                log.info("Finaliza proceso de consultar la competencia asociada al resultado con id = {0}", resultadoId);
                return resultadoEntity.get().getCompetenciaRelacionada();
        }
    

        //Actualizar o reemplazar la competencia asociada del resultado.
        @Transactional
        public CompetenciaEntity replaceCompetenciaAsociada(Long resultadoId, CompetenciaEntity remplazo) throws IllegalOperationException, EntityNotFoundException {
                log.info("Inicia proceso de reemplazar la competencia asociada al resultado con id = {0}", resultadoId);
                Optional<ResultadoFinalEntity> resultadoEntity = resultadoFinalRepository.findById(resultadoId);

                if (remplazo == null)
                        throw new IllegalOperationException("No se puede remplazar por una competencia nula.");

                if (resultadoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el resultado con ese identificador.");
    
                Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(remplazo.getId());
                    if (competenciaEntity.isEmpty())
                                throw new EntityNotFoundException("No se ha encontrado la competencia con la que se va a actualizar.");
    
                        if (!resultadoEntity.get().getCompetenciaRelacionada().equals(remplazo))
                                resultadoEntity.get().setCompetenciaRelacionada(remplazo);
                
                log.info("Termina proceso de reemplazar la competencia asociada del resultado con id = {0}", resultadoId);
                return getCompetenciaAsociada(resultadoId);
        }
    
        //Quitar una competencia asociada de un Resultado
        @Transactional
        public void removeCompetenciaAsociada(Long resultadoId) throws EntityNotFoundException {
                log.info("Inicia proceso de borrar una competencia de un resultado con id = {0}", resultadoId);

                Optional<ResultadoFinalEntity> resultadoEntity = resultadoFinalRepository.findById(resultadoId);
    
                if (resultadoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el resultado final con ese id.");
    
                resultadoEntity.get().setCompetenciaRelacionada(null);
    
                log.info("Termina proceso de borrar una competencia asciada al resultado con id = {0}", resultadoId);
        }
        
    
}
