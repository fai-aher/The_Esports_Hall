package co.edu.uniandes.dse.esports.services;

import java.util.Optional;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

// Esta clase se usa para modelar operaciones que tiene asociada una competencia con respecto a sus
// resultados finales

/* EN ESTA ASOCIACION, RESULTADOS FINALES ES LA ENTIDAD DUENA DE LA RELACION */
public class CompetenciaResultadoFinalService {
    
    @Autowired
    private CompetenciaRepository competenciaRepository;

     //Consultar los resultados finales de una competencia
     @Transactional
     public List<ResultadoFinalEntity> getResultadosFinales(Long competenciaId) throws EntityNotFoundException 
     {
         log.info("Inicia proceso de consultar los resultados finales de la competencia con id = {0}", competenciaId);
         Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
         if (competenciaEntity.isEmpty())
                 throw new EntityNotFoundException("No se ha encontrado una competencia con el id proporcionado");
         log.info("Finaliza proceso de consultar los resultados finales de la competencia con id = {0}", competenciaId);
         return competenciaEntity.get().getResultadosFinales();
     }
 }
