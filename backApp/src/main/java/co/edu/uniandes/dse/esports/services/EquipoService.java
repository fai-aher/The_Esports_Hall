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
import co.edu.uniandes.dse.esports.repositories.JugadorRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service //Indica que es un servicio de Spring que puede
        //usarse en otras partes del código por medio de inyeccion
        //de dependencias.

public class EquipoService {


    private static final String MENSAJELOGO = "El equipo debe tener un logo asociado";
    private static final String MENSAJEBANDERA = "El equipo debe tener una bandera de un pais asociada.";
    private static final String MENSAJENOMBRE = "El equipo debe tener un nombre asociado.";
    private static final String MENSAJEPAIS = "El equipo debe tener un pais de procedencia asociado.";


    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    JugadorRepository jugadorRepository;

    //adicionar una para CompetenciaRepository?

    /* Metodos */

    // 1. Crear
    @Transactional
    public EquipoEntity createEquipo(EquipoEntity equipoEntity) throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Se ha comenzado a crear un nuevo equipo.");


        //validar que datos del nuevo equipo no existan ya.
        
        //validar que no exista otro equipo con el mismo id (que implicaria un mismo nombre y nacionalidad: restriccion de negocio).

        Optional<EquipoEntity> alreadyExists = equipoRepository.findById(equipoEntity.getId());

        if (!alreadyExists.isEmpty())
                throw new IllegalOperationException("Ya existe el equipo que se quiere crear.");

        //Validar que los datos del nuevo equipo sean los necesarios

        if (equipoEntity.getLogo() == null)
                throw new IllegalOperationException(MENSAJELOGO);

        if (equipoEntity.getLogo().equals(""))
                throw new IllegalOperationException(MENSAJELOGO);

        if (equipoEntity.getBanderaPais() == null)
                throw new IllegalOperationException(MENSAJEBANDERA);

        if (equipoEntity.getBanderaPais().equals(""))
                throw new IllegalOperationException(MENSAJEBANDERA);

        if (equipoEntity.getNombre() == null)
                throw new IllegalOperationException(MENSAJENOMBRE);

        if (equipoEntity.getNombre().equals(""))
                throw new IllegalOperationException(MENSAJENOMBRE);

        if (equipoEntity.getPaisProcedencia() == null)
                throw new IllegalOperationException(MENSAJEPAIS);

         if (equipoEntity.getPaisProcedencia().equals(""))
                throw new IllegalOperationException(MENSAJEPAIS);


        log.info("Termina la creacion de un nuevo equipo.");

        return equipoRepository.save(equipoEntity);

    }


    // Encontrar todos los equipos

    @Transactional
    public List<EquipoEntity> getAllEquipos() {

        log.info("Comienza el proceso de buscar todos los equipos del repositorio.");

        return equipoRepository.findAll();

    }

    // Encontrar un equipo

    @Transactional
    public EquipoEntity getEquipo(Long equipoID) throws EntityNotFoundException {
        
        log.info("Comienza el proceso para buscar un equipo con la id = {0}", equipoID);

        Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoID);

        if (equipoEntity.isEmpty())
                throw new EntityNotFoundException("No se ha encontrado un equipo con el ID suministrado.");
        
        log.info("Finaliza el proceso para buscar un equipo con la id = {0}", equipoID);

        return equipoEntity.get();
    }

    // Actualizar un equipo

    @Transactional
    public EquipoEntity updateEquipo(Long equipoID, EquipoEntity nuevaVerEquipo) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia el proceso de actualizar el equipo con id = %s", equipoID);

        Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoID);

        if (!equipoEntity.isPresent()) {
                // El equipo no fue encontrado en la base de datos
                throw new EntityNotFoundException("El equipo con ID " + equipoID + " no fue encontrado en la base de datos.");
            }

        if (nuevaVerEquipo.getCompetenciasParticipadas() == null || nuevaVerEquipo.getCompetenciasParticipadas().isEmpty() ){
                throw new IllegalOperationException("El equipo debe tener competencias participadas para ser actualizado.");
            }
        
        if (nuevaVerEquipo.getLogo() == null || nuevaVerEquipo.getLogo().equals("")) {
                throw new IllegalOperationException(MENSAJELOGO);
            }
        
        if (nuevaVerEquipo.getBanderaPais() == null || nuevaVerEquipo.getBanderaPais().equals("")) {
                throw new IllegalOperationException(MENSAJEBANDERA);
            }
        
        if (nuevaVerEquipo.getNombre() == null || nuevaVerEquipo.getNombre().equals("")) {
                throw new IllegalOperationException(MENSAJENOMBRE);
            }
        
        if (nuevaVerEquipo.getPaisProcedencia() == null || nuevaVerEquipo.getPaisProcedencia().equals("")) {
                throw new IllegalOperationException(MENSAJEPAIS);
            }


        nuevaVerEquipo.setId(equipoID);

        log.info("Termina el proceso de actualizar el equipo con id = %s", equipoID);

        return equipoRepository.save(nuevaVerEquipo);
    }

    // Borrar un Equipo
    @Transactional
    public void deleteEquipo(Long equipoID) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia el proceso para borrar el equipo con id = {0}", equipoID);

        Optional<EquipoEntity> equipoEntity = equipoRepository.findById(equipoID);

        if (equipoEntity.isEmpty())
                throw new EntityNotFoundException("No se encontro un equipo con el Identificador proporcionado.");

        // No puede estar vinculado a una competencia, pues en ese caso el equipo es necesario que exista.

        List<CompetenciaEntity> competenciasParticipadas = equipoEntity.get().getCompetenciasParticipadas();

        if (!competenciasParticipadas.isEmpty())
                throw new IllegalOperationException("Debido a que existen competencias donde este equipo participa, no es posible borrarlo de la base de datos.");

        //Borrado
        equipoRepository.deleteById(equipoID);

        log.info("Termina el proceso para borrar el equipo con id = {0}", equipoID);

    }

    
    
}
