package co.edu.uniandes.dse.esports.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.uniandes.dse.esports.dto.CompetenciaDTO;
import co.edu.uniandes.dse.esports.services.EquipoCompetenciaService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

@RestController
@RequestMapping("/equipos/{idEquipo}/competenciasParticipadas")
public class EquipoCompetenciasParticipadasController {

    @Autowired
    private EquipoCompetenciaService equipoCompetenciasParticipadasService;

    @Autowired
    private ModelMapper modelMapper;

    // PRIMERO: Para la relación con competenciasParticipadas

    /**
     * Obtiene todas las competencias en las que un equipo ha participado
     *
     * @param idEquipo Identificador del equipo del cual se desean obtener las competencias
     * @return JSON {@link CompetenciaDTO} - Las competencias encontradas en las que el equipo ha participado
     * @throws EntityNotFoundException
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CompetenciaDTO> getCompetenciasByEquipo(@PathVariable Long idEquipo) throws EntityNotFoundException {
        List<CompetenciaEntity> competencias = equipoCompetenciasParticipadasService.getCompetenciasParticipadas(idEquipo);
        return modelMapper.map(competencias, new TypeToken<List<CompetenciaDTO>>() {
        }.getType());
    }

    /**
     * Obtiene una competencias en las que un equipo ha participado
     *
     * @param idEquipo Identificador del equipo del cual se desean obtener las competencias
     * @return JSON {@link CompetenciaDTO} - Las competencias encontradas en las que el equipo ha participado
     * @throws EntityNotFoundException
     */
    @GetMapping(value = "/{idCompetencia}")
    @ResponseStatus(code = HttpStatus.OK)
    public CompetenciaDTO getCompetenciaByEquipo(@PathVariable Long idEquipo, @PathVariable Long idCompetencia) throws EntityNotFoundException, IllegalOperationException {
        CompetenciaEntity competencia = equipoCompetenciasParticipadasService.getCompetenciaParticipada(idEquipo, idCompetencia);
        return modelMapper.map(competencia, new TypeToken<CompetenciaDTO>() {
        }.getType());
    }

    /**
     * Asocia una competencia a un equipo
     *
     * @param idEquipo       Identificador del equipo al cual se desea asociar la competencia
     * @param idCompetencia  Identificador de la competencia que se desea asociar al equipo
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{idCompetencia}")
    @ResponseStatus(code = HttpStatus.OK)
    public void addCompetenciaToEquipo(
            @PathVariable Long idEquipo,
            @PathVariable Long idCompetencia) throws EntityNotFoundException, IllegalOperationException {
        equipoCompetenciasParticipadasService.addCompetenciaParticipada(idEquipo, idCompetencia);
    }

    /**
     * Reemplaza todas las competencias participadas de un equipo con una nueva lista de competencias.
     *
     * @param idEquipo Identificador del equipo al cual se le actualizarán las competencias.
     * @param competenciasDTO Lista de competencias DTO que se desea asociar al equipo.
     * @return List<CompetenciaDTO> - Lista de competencias actualizada asociada al equipo.
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CompetenciaDTO> replaceCompetenciasOfEquipo(
            @PathVariable Long idEquipo,
            @RequestBody List<CompetenciaDTO> competenciasDTO) throws EntityNotFoundException, IllegalOperationException {
        List<CompetenciaEntity> competenciasEntity = modelMapper.map(competenciasDTO, new TypeToken<List<CompetenciaEntity>>() {}.getType());
        List<CompetenciaEntity> updatedCompetencias = equipoCompetenciasParticipadasService.replaceCompetenciasParticipadas(idEquipo, competenciasEntity);
        return modelMapper.map(updatedCompetencias, new TypeToken<List<CompetenciaDTO>>() {}.getType());
    }

    /**
     * Desasocia una competencia de un equipo
     *
     * @param idEquipo       Identificador del equipo del cual se desea desasociar la competencia
     * @param idCompetencia  Identificador de la competencia que se desea desasociar del equipo
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @DeleteMapping("/{idCompetencia}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCompetenciaFromEquipo(
            @PathVariable Long idEquipo,
            @PathVariable Long idCompetencia) throws EntityNotFoundException{
        equipoCompetenciasParticipadasService.removeCompetenciaParticipada(idEquipo, idCompetencia);
    }

}