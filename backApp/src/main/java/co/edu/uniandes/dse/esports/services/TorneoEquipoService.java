package co.edu.uniandes.dse.esports.services;

import co.edu.uniandes.dse.esports.repositories.TorneoRepository;
import co.edu.uniandes.dse.esports.repositories.EquipoRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;

@Slf4j
@Service
public class TorneoEquipoService {
    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    /**
         * Obtiene una colección de instancias de EquipoEntity asociadas a una instancia
         * de Torneo
         *
         * @param torneoId Identificador de la instancia de Torneo
         * @return Colección de instancias de EquipoEntity asociadas a la instancia de
         *         Torneo
         */
        @Transactional
        public List<EquipoEntity> getEquipos(Long torneoId) throws EntityNotFoundException {
                log.info("Inicia proceso de consultar todos los equipos del torneo con id = {0}", torneoId);
                Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);
                if (torneoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el torneo con el id dado");
                log.info("Finaliza proceso de consultar todos los equipos del torneo con id = {0}", torneoId);
                return torneoEntity.get().getEquiposParticipantes();
        }

        /**
         * Obtiene una instancia de EquipoEntity asociada a una instancia de Torneo
         *
         * @param torneoId   Identificador de la instancia de Torneo
         * @param equipoId Identificador de la instancia de Equipo
         * @return La entidad del equipo asociada al torneo
         */
        @Transactional
        public EquipoEntity getEquipo(Long torneoId, Long equipoId)
                        throws EntityNotFoundException, IllegalOperationException {
                log.info("Inicia proceso de consultar un equipo del torneo con id = {0}", torneoId);
                Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoId);
                Optional<TorneoEntity> torneoEntity = torneoRepository.findById(torneoId);

                if (equipoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el equipo con el id dado");

                if (torneoEntity.isEmpty())
                        throw new EntityNotFoundException("No se ha encontrado el torneo con el id dado");
                log.info("Termina proceso de consultar un equipo del torneo con id = {0}", torneoId);
                if (torneoEntity.get().getEquiposParticipantes().contains(equipoEntity.get()))
                        return equipoEntity.get();

                throw new IllegalOperationException("El equipo no está asociado con el torneo");
        }
}
