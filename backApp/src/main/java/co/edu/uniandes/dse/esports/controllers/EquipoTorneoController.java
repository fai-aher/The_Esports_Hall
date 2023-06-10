package co.edu.uniandes.dse.esports.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.esports.dto.TorneoDTO;
import co.edu.uniandes.dse.esports.services.EquipoTorneoService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.TorneoEntity;

@RestController
@RequestMapping("/equipos/{idEquipo}/torneosParticipados")
public class EquipoTorneoController {

    @Autowired
    private EquipoTorneoService equipoTorneosService;

    @Autowired
    private ModelMapper modelMapper;

    // 1. Agregar un solo torneo a la lista de 'torneosParticipados' de un equipo
    @PostMapping("/{idTorneo}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public TorneoDTO addTorneoToEquipo(@PathVariable Long idEquipo, @PathVariable Long idTorneo) throws EntityNotFoundException {
        TorneoEntity addedTorneo = equipoTorneosService.addTorneoParticipado(idEquipo, idTorneo);
        return modelMapper.map(addedTorneo, TorneoDTO.class);
    }

    // 3. Obtener todos los 'torneosParticipados' de un equipo
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TorneoDTO> getTorneosParticipadosByEquipo(@PathVariable Long idEquipo) throws EntityNotFoundException {
        List<TorneoEntity> torneos = equipoTorneosService.getTorneosParticipados(idEquipo);
        return modelMapper.map(torneos, new TypeToken<List<TorneoDTO>>() {}.getType());
    }

    // 4. Obtener solo un 'torneoParticipado' de un equipo
    @GetMapping("/{idTorneo}")
    @ResponseStatus(code = HttpStatus.OK)
    public TorneoDTO getTorneoParticipadoByEquipo(@PathVariable Long idEquipo, @PathVariable Long idTorneo) throws EntityNotFoundException, IllegalOperationException {
        TorneoEntity torneoEntity = equipoTorneosService.getTorneoParticipado(idEquipo, idTorneo);
        return modelMapper.map(torneoEntity, TorneoDTO.class);
    }

    // 5. Remplazar todos los torneos participados de un equipo
    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TorneoDTO> replaceTorneosParticipados(@PathVariable Long idEquipo, @RequestBody List<TorneoDTO> torneosDTO) throws EntityNotFoundException {
        List<TorneoEntity> torneosEntity = modelMapper.map(torneosDTO, new TypeToken<List<TorneoEntity>>() {}.getType());
        List<TorneoEntity> replacedTorneos = equipoTorneosService.replaceTorneosParticipados(idEquipo, torneosEntity);
        return modelMapper.map(replacedTorneos, new TypeToken<List<TorneoDTO>>() {}.getType());
    }

    // 6. Borrar un torneo participado de un equipo
    @DeleteMapping("/{idTorneo}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeTorneoParticipadoFromEquipo(@PathVariable Long idEquipo, @PathVariable Long idTorneo) throws EntityNotFoundException {
        equipoTorneosService.removeTorneoParticipado(idEquipo, idTorneo);
    }
}