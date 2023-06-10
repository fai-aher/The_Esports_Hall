package co.edu.uniandes.dse.esports.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.esports.dto.ResultadoFinalDTO;
import co.edu.uniandes.dse.esports.services.EquipoResultadoFinalService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;

@RestController
@RequestMapping("/equipos/{idEquipo}/resultados")
public class EquipoResultadoFinalController {

    @Autowired
    private EquipoResultadoFinalService equipoResultadoFinalService;

    @Autowired
    private ModelMapper modelMapper;

    // 1. Obtener un resultado obtenido en una competencia
    @GetMapping("/{idCompetencia}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResultadoFinalDTO getResultadoByCompetencia(@PathVariable Long idEquipo, @PathVariable Long idCompetencia) throws EntityNotFoundException, IllegalOperationException {
        ResultadoFinalEntity resultadoEntity = equipoResultadoFinalService.getResultadoDeUnaCompetencia(idEquipo, idCompetencia);
        return modelMapper.map(resultadoEntity, ResultadoFinalDTO.class);
    }

    // 2. Obtener todos los resultados obtenidos por un equipo
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResultadoFinalDTO> getAllResultados(@PathVariable Long idEquipo) throws EntityNotFoundException {
        List<ResultadoFinalEntity> resultados = equipoResultadoFinalService.getResultadoFinalEntities(idEquipo);
        return modelMapper.map(resultados, new TypeToken<List<ResultadoFinalDTO>>() {
        }.getType());
    }
}