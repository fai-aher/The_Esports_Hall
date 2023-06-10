package co.edu.uniandes.dse.esports.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.esports.dto.EquipoDTO;
import co.edu.uniandes.dse.esports.services.ResultadoFinalEquipoService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;

@RestController
@RequestMapping("/resultados/{idResultadoFinal}/equipoInvolucrado")
public class ResultadoFinalEquipoController {

    @Autowired
    private ResultadoFinalEquipoService resultadoFinalEquipoService;

    @Autowired
    private ModelMapper modelMapper;

    // 1. Relacionar un equipo a una Resultado Final
    @PostMapping("/{idEquipo}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EquipoDTO addEquipoToResultadoFinal(@PathVariable Long idResultadoFinal, @PathVariable Long idEquipo) throws EntityNotFoundException, IllegalOperationException {
        EquipoEntity equipoEntity = resultadoFinalEquipoService.addEquipoInvolucrado(idResultadoFinal, idEquipo);
        return modelMapper.map(equipoEntity, EquipoDTO.class);
    }

    // 2. Obtener el Equipo Involucrado de un resultado final
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public EquipoDTO getEquipoInvolucrado(@PathVariable Long idResultadoFinal) throws EntityNotFoundException {
        EquipoEntity equipoEntity = resultadoFinalEquipoService.getEquipoInvolucrado(idResultadoFinal);
        return modelMapper.map(equipoEntity, EquipoDTO.class);
    }

    // 3. Remplazar el equipo involucrado de un resultado final
    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public EquipoDTO replaceEquipoInvolucrado(@PathVariable Long idResultadoFinal, @RequestBody EquipoDTO equipoDTO) throws EntityNotFoundException {
        EquipoEntity equipo = modelMapper.map(equipoDTO, EquipoEntity.class);
        EquipoEntity equipoEntity = resultadoFinalEquipoService.replaceEquipoInvolucrado(idResultadoFinal, equipo);
        return modelMapper.map(equipoEntity, EquipoDTO.class);
    }

    // 4. Eliminar el equipo involucrado del resultado final
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeEquipoInvolucrado(@PathVariable Long idResultadoFinal) throws EntityNotFoundException {
        resultadoFinalEquipoService.removeEquipoInvolucrado(idResultadoFinal);
    }
}