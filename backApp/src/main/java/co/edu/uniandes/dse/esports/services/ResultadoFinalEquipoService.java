package co.edu.uniandes.dse.esports.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.ResultadoFinalRepository;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociado un equipo con respecto a sus
// Competencias Participadas

/* EN ESTA ASOCIACION, RESULTADO FINAL ES LA ENTIDAD DUENA DE LA RELACION */

public class ResultadoFinalEquipoService {

    @Autowired
    private ResultadoFinalRepository resultadoFinalRepository;

    @Autowired
    private EquipoRepository equipoRepository;

        //Adicionar un equipoAsociado a un resultadoFinal.
        @Transactional
        public EquipoEntity addEquipoInvolucrado(Long resultadoFinalId, Long equipoId) throws EntityNotFoundException, IllegalOperationException {
                log.info("Inicia proceso de asociarle un equipo involucrado al resultado con id = {0}", resultadoFinalId);

                if (equipoId == null)
                        throw new IllegalOperationException("No es posible determinar el equipo porque el id pasado por parametro no tiene valor.");

                Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
                if (equipoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el equipo con el id pasado por parametro.");
    
                Optional<ResultadoFinalEntity> resultadoEntity = resultadoFinalRepository.findById(resultadoFinalId);
                if (resultadoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el resultado con el id");
    
                resultadoEntity.get().setEquipoInvolucrado(equipoEntity.get());
                log.info("Termina proceso de asociarle un equipo involucrado al resultado con id = {0}", resultadoFinalId);
                return equipoEntity.get();
    
        }
    
        //Consultar el equipo asociado con un resultado
        @Transactional
        public EquipoEntity getEquipoInvolucrado(Long resultadoId) throws EntityNotFoundException {
                log.info("Inicia proceso de consultar el equipo involucrado con el resultado con id = {0}", resultadoId);
                Optional<ResultadoFinalEntity> resultadoEntity = resultadoFinalRepository.findById(resultadoId);
                if (resultadoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el resultado con la id pasada por parametro.");
                log.info("Finaliza proceso de consultar el equipo involucrado al resultado con id = {0}", resultadoId);
                return resultadoEntity.get().getEquipoInvolucrado();
        }
    

        //Actualizar o reemplazar el equipo asociado del resultado.
        @Transactional
        public EquipoEntity replaceEquipoInvolucrado(Long resultadoId, EquipoEntity remplazo) throws EntityNotFoundException {
                log.info("Inicia proceso de reemplazar el equipo Involucrado al resultado con id = {0}", resultadoId);
                Optional<ResultadoFinalEntity> resultadoEntity = resultadoFinalRepository.findById(resultadoId);
                if (resultadoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el resultado con ese identificador.");
    
                Optional<EquipoEntity> equipoEntity = equipoRepository.findById(remplazo.getId());
                    if (equipoEntity.isEmpty())
                                throw new EntityNotFoundException("No se ha encontrado el equipo con el que se va a actualizar.");
    
                        if (!resultadoEntity.get().getEquipoInvolucrado().equals(remplazo))
                                resultadoEntity.get().setEquipoInvolucrado(remplazo);
                
                log.info("Termina proceso de reemplazar el equipo involucrado del resultado con id = {0}", resultadoId);
                return getEquipoInvolucrado(resultadoId);
        }
    
        //Quitar el equipo involucrado de un resultado
        @Transactional
        public void removeEquipoInvolucrado(Long resultadoId) throws EntityNotFoundException {
                log.info("Inicia proceso de borrar el equipo asociado de un resultado con id = {0}", resultadoId);

                Optional<ResultadoFinalEntity> resultadoEntity = resultadoFinalRepository.findById(resultadoId);
    
                if (resultadoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el resultado final con ese id.");
    
                resultadoEntity.get().setEquipoInvolucrado(null);
    
                log.info("Termina proceso de borrar el equipo involucrado al resultado con id = {0}", resultadoId);
        }
        
    
}
