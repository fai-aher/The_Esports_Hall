package co.edu.uniandes.dse.esports.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.esports.dto.CompetenciaDTO;
import co.edu.uniandes.dse.esports.services.EquipoCompetenciasGanadasService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

@RestController
@RequestMapping("/equipos/{idEquipo}/competenciasGanadas")
public class EquipoCompetenciasGanadasController {

    @Autowired
    private EquipoCompetenciasGanadasService equipoCompetenciasGanadasService;

    @Autowired
    private ModelMapper modelMapper;

       /**
     * Obtiene todas las competencias ganadas por un equipo en particular
     *
     * @param idEquipo Identificador del equipo del cual se desean obtener las competencias ganadas
     * @return JSON {@link CompetenciaDTO} - Las competencias ganadas por el equipo
     * @throws EntityNotFoundException
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CompetenciaDTO> getCompetenciasGanadasByEquipo(@PathVariable Long idEquipo) throws EntityNotFoundException {
        List<CompetenciaEntity> competencias = equipoCompetenciasGanadasService.getCompetenciasGanadas(idEquipo);
        return modelMapper.map(competencias, new TypeToken<List<CompetenciaDTO>>() {}.getType());
    }
    
    /**
     * Obtiene una competencia ganada por un equipo en particular
     *
     * @param idEquipo Identificador del equipo del cual se desea obtener una competencia ganada
     * @param idCompetencia Identificador de la competencia que se desea obtener
     * @return JSON {@link CompetenciaDTO} - La competencia ganada por el equipo
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping(value = "/{idCompetencia}")
    @ResponseStatus(code = HttpStatus.OK)
    public CompetenciaDTO getCompetenciaGanadaByEquipo(@PathVariable Long idEquipo, @PathVariable Long idCompetencia) throws EntityNotFoundException, IllegalOperationException {
        CompetenciaEntity competencia = equipoCompetenciasGanadasService.getCompetenciaGanada(idEquipo, idCompetencia);
        return modelMapper.map(competencia, new TypeToken<CompetenciaDTO>() {}.getType());
    }

    
}
