package co.edu.uniandes.dse.esports.services;

import co.edu.uniandes.dse.esports.repositories.TorneoRepository;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;

@Slf4j
@Service
public class TorneoCompetenciaService {
    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

    /**
         * Obtiene una colección de instancias de CompetenciaEntity asociadas a una instancia
         * de Torneo
         *
         * @param torneoId Identificador de la instancia de Torneo
         * @return Colección de instancias de CompetenciaEntity asociadas a la instancia de
         *         Torneo
         */
        @Transactional
        public List<CompetenciaEntity> getCompetencias(Long torneoId) throws EntityNotFoundException {
                log.info("Inicia proceso de consultar todas las competencias del torneo con id = {0}", torneoId);
                Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
                if (torneoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el torneo con el id dado");
                log.info("Finaliza proceso de consultar todas las competencias del torneo con id = {0}", torneoId);
                return torneoEntity.get().getCompetencias();
        }

        /**
         * Obtiene una instancia de CompetenciaEntity asociada a una instancia de Torneo
         *
         * @param torneoId   Identificador de la instancia de Torneo
         * @param competenciaId Identificador de la instancia de Competencia
         * @return La entidad de la competencia asociada al torneo
         */
        @Transactional
        public CompetenciaEntity getCompetencia(Long torneoId, Long competenciaId)
                        throws EntityNotFoundException, IllegalOperationException {
                log.info("Inicia proceso de consultar una competencia del torneo con id = {0}", torneoId);
                Optional<CompetenciaEntity> competenciaEntity = competenciaRepository.findById(competenciaId);
                Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);

                if (competenciaEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado la competencia con el id dado");

                if (torneoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el torneo con el id dado");
                log.info("Termina proceso de consultar una competencia del torneo con id = {0}", torneoId);
                if (torneoEntity.get().getCompetencias().contains(competenciaEntity.get()))
                        return competenciaEntity.get();

                throw new IllegalOperationException("La competencia no está asociada con el torneo");
        }
}
