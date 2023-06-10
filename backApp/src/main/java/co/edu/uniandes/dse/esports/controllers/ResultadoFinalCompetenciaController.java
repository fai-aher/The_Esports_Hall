package co.edu.uniandes.dse.esports.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.esports.dto.CompetenciaDTO;
import co.edu.uniandes.dse.esports.services.ResultadoFinalCompetenciaService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

@RestController
@RequestMapping("/resultados/{idResultadoFinal}/competenciaRelacionada")
public class ResultadoFinalCompetenciaController {

    @Autowired
    private ResultadoFinalCompetenciaService resultadoFinalCompetenciaService;

    @Autowired
    private ModelMapper modelMapper;

    // 1. Relacionar una competencia a una Resultado Final
    @PostMapping("/{idCompetencia}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CompetenciaDTO addCompetenciaToResultadoFinal(@PathVariable Long idResultadoFinal, @PathVariable Long idCompetencia) throws EntityNotFoundException, IllegalOperationException {
        CompetenciaEntity competenciaEntity = resultadoFinalCompetenciaService.addCompetenciaAsociada(idResultadoFinal, idCompetencia);
        return modelMapper.map(competenciaEntity, CompetenciaDTO.class);
    }

    // 2. Obtener la Competencia Relacionada de un resultado final
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public CompetenciaDTO getCompetenciaRelacionada(@PathVariable Long idResultadoFinal) throws EntityNotFoundException {
        CompetenciaEntity competenciaEntity = resultadoFinalCompetenciaService.getCompetenciaAsociada(idResultadoFinal);
        return modelMapper.map(competenciaEntity, CompetenciaDTO.class);
    }

    // 3. Remplazar la competencia relacionada de un resultado final
    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public CompetenciaDTO replaceCompetenciaRelacionada(@PathVariable Long idResultadoFinal, @RequestBody CompetenciaDTO competenciaDTO) throws EntityNotFoundException, IllegalOperationException {
        CompetenciaEntity competencia = modelMapper.map(competenciaDTO, CompetenciaEntity.class);
        CompetenciaEntity competenciaEntity = resultadoFinalCompetenciaService.replaceCompetenciaAsociada(idResultadoFinal, competencia);
        return modelMapper.map(competenciaEntity, CompetenciaDTO.class);
    }

    // 4. Eliminar la competencia relacionada del resultado final
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCompetenciaRelacionada(@PathVariable Long idResultadoFinal) throws EntityNotFoundException {
        resultadoFinalCompetenciaService.removeCompetenciaAsociada(idResultadoFinal);
    }
}