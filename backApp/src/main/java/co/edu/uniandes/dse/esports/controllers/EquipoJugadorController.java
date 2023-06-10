package co.edu.uniandes.dse.esports.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.esports.dto.JugadorDTO;
import co.edu.uniandes.dse.esports.services.EquipoJugadorService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;

@RestController
@RequestMapping("/equipos/{idEquipo}/jugadores")
public class EquipoJugadorController {

    @Autowired
    private EquipoJugadorService equipoJugadorService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todos los jugadores de un equipo.
     *
     * @param idEquipo Identificador del equipo para el cual se buscan los jugadores.
     * @return List<JugadorDTO> - Lista de jugadores en el equipo.
     * @throws EntityNotFoundException
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<JugadorDTO> getJugadoresByEquipo(@PathVariable Long idEquipo) throws EntityNotFoundException {
        List<JugadorEntity> jugadores = equipoJugadorService.getIntegrantes(idEquipo);
        return modelMapper.map(jugadores, new TypeToken<List<JugadorDTO>>() {}.getType());
    }

    /**
     * Obtiene un jugador específico de un equipo.
     *
     * @param idEquipo Identificador del equipo al cual pertenece el jugador.
     * @param idJugador Identificador del jugador que se desea obtener.
     * @return JugadorDTO - Jugador encontrado.
     * @throws EntityNotFoundException
     */
    @GetMapping("/{idJugador}")
    @ResponseStatus(code = HttpStatus.OK)
    public JugadorDTO getJugadorByEquipo(@PathVariable Long idEquipo, @PathVariable Long idJugador) throws EntityNotFoundException, IllegalOperationException {
        JugadorEntity jugadorEntity = equipoJugadorService.getIntegrante(idEquipo, idJugador);
        return modelMapper.map(jugadorEntity, JugadorDTO.class);
    }
}