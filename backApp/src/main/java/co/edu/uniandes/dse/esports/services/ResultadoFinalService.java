package co.edu.uniandes.dse.esports.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;
import co.edu.uniandes.dse.esports.repositories.ResultadoFinalRepository;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

public class ResultadoFinalService {

    @Autowired
    ResultadoFinalRepository resultadoFinalRepository;

    @Autowired
    EquipoRepository equipoRepository;
    
    @Autowired
    CompetenciaRepository competenciaRepository;

    /* Metodos */

    // 1. Crear
    @Transactional
    public ResultadoFinalEntity createResultadoFinal(ResultadoFinalEntity resultadoFinalEntity) throws EntityNotFoundException, IllegalOperationException {

        log.info("Se ha comenzado a crear un nuevo resultado de una competencia.");

        //competenciaRelacionada y equipoInvolucrado

        CompetenciaEntity competenciaCheck = resultadoFinalEntity.getCompetenciaRelacionada();

        EquipoEntity equipoCheck = resultadoFinalEntity.getEquipoInvolucrado();

        if (competenciaCheck == null)
                throw new IllegalOperationException("Es necesario que el resultado final este vinculado a una competencia.");

        if (equipoCheck == null)
                throw new IllegalOperationException("Es necesario que el resultado final este vinculado a un equipo.");

        //Validacion de atributos necesarios

        if (resultadoFinalEntity.getPosicionFinal() == null)
                throw new IllegalOperationException("No es posible crear un resultado final sin la posicion final que alcanzo el equipo en esa competencia.");


        if (resultadoFinalEntity.getParteDeEmpate() == null)
                throw new IllegalOperationException("Debe especificarse si el equipo hizo parte de un empate en esa competencia o no.");


        Optional<ResultadoFinalEntity> alreadyExists = resultadoFinalRepository.findById(resultadoFinalEntity.getId());     
        
        if (!alreadyExists.isEmpty())
                throw new IllegalOperationException("Ya existe el resultadoFinal que se quiere crear.");

        
        log.info("Termina la creacion de un nuevo resultado final.");

        return resultadoFinalRepository.save(resultadoFinalEntity);
    }


    // Encontrar todos los resultados finales

    @Transactional
    public List<ResultadoFinalEntity> getAllResultadosFinales() {

        log.info("Comienza el proceso de buscar todos los resultados finales del repositorio.");

        return resultadoFinalRepository.findAll();

    }

    // Encontrar un resultado final

    @Transactional
    public ResultadoFinalEntity getResultadoFinal(Long resultadoID) throws EntityNotFoundException {
        
        log.info("Comienza el proceso para buscar un resultado final con la id = {0}", resultadoID);

        Optional<ResultadoFinalEntity> resultadoFinalEntity = resultadoFinalRepository.findById(resultadoID);

        if (resultadoFinalEntity.isEmpty())
                throw new EntityNotFoundException("No se ha encontrado un resultado final con el ID suministrado.");
        
        log.info("Finaliza el proceso para buscar un resultado final con la id = {0}", resultadoID);

        return resultadoFinalEntity.get();
    } 

    // Actualizar un resultado final

    @Transactional
    public ResultadoFinalEntity updateResultadoFinal(Long resultadoID, ResultadoFinalEntity nuevaVerResultado) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia el proceso de actualizar el resultado final con id = {0}", resultadoID);

        Optional<ResultadoFinalEntity> resultadoFinalEntity = resultadoFinalRepository.findById(resultadoID);

        if (resultadoFinalEntity.isEmpty())
                throw new EntityNotFoundException("No se encontro un resultado final con ese Identificador.");

        if (nuevaVerResultado.getCompetenciaRelacionada() == null)
                throw new IllegalOperationException("No se puede actualizar porque el resultado debe tener una competencia relacionada.");
            
        if (nuevaVerResultado.getEquipoInvolucrado() == null)
                throw new IllegalOperationException("No se puede actualizar porque el resultado debe tener un equipo relacionado.");
            
        if (nuevaVerResultado.getPosicionFinal() == null)
                throw new IllegalOperationException("No se puede actualizar porque el resultado debe tener una posicion final para el equipo.");

        if (nuevaVerResultado.getParteDeEmpate() == null)
                throw new IllegalOperationException("No se puede actualizar porque el resultado debe especificar si el equipo fue parte o no de un empate.");

        nuevaVerResultado.setId(resultadoID);

        log.info("Termina el proceso de actualizar el resultado final con id = {0}", resultadoID);

        return resultadoFinalRepository.save(nuevaVerResultado);
    }

        // Borrar un Resultado final
        @Transactional
        public void deleteResultadoFinal(Long resultadoID) throws EntityNotFoundException, IllegalOperationException {
    
            log.info("Inicia el proceso para borrar el resultado final con id = {0}", resultadoID);
    
            Optional<ResultadoFinalEntity> resultadoFinalEntity = resultadoFinalRepository.findById(resultadoID);
    
            if (resultadoFinalEntity.isEmpty())
                    throw new EntityNotFoundException("No se encontro un resultado final con el Identificador proporcionado.");
    
            // No puede estar vinculado a una competencia o equipo, pues en ese caso el equipo es necesario que exista.
    
            CompetenciaEntity competenciaRelacionada = resultadoFinalEntity.get().getCompetenciaRelacionada();
            EquipoEntity equipoVinculado = resultadoFinalEntity.get().getEquipoInvolucrado();
    
            if (competenciaRelacionada != null)
                    throw new IllegalOperationException("Debido a que esta vinculado a una competencia, no es posible borrar el resultado de la base de datos.");
    
            if (equipoVinculado != null)
                    throw new IllegalOperationException("Debido a que esta vinculado a un equipo, no es posible borrar el resultado de la base de datos.");
    
            //Borrado
            resultadoFinalRepository.deleteById(resultadoID);
    
            log.info("Termina el proceso para borrar el resultado con id = {0}", resultadoID);
    
        }
}
