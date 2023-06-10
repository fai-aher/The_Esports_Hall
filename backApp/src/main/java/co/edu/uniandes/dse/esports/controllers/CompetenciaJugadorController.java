package co.edu.uniandes.dse.esports.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.esports.dto.JugadorDetailDTO;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.services.CompetenciaJugadorService;

@RestController
@RequestMapping("/competencias")
public class CompetenciaJugadorController {

    @Autowired
	private CompetenciaJugadorService competenciaJugadorService;

	@Autowired
	private ModelMapper modelMapper;

    @PostMapping(value = "/{competenciaId}/jugadores/{jugadorId}")
	@ResponseStatus(code = HttpStatus.OK)
	public JugadorDetailDTO addJugador(@PathVariable Long jugadorId, @PathVariable Long competenciaId)
			throws EntityNotFoundException {
		JugadorEntity jugadorEntity = competenciaJugadorService.addJugador(jugadorId, competenciaId);
		return modelMapper.map(jugadorEntity, JugadorDetailDTO.class);
	}

    @GetMapping(value = "/{competenciaId}/jugadores")
	@ResponseStatus(code = HttpStatus.OK)
	public JugadorDetailDTO getJugador(@PathVariable Long competenciaId)
			throws EntityNotFoundException {
		JugadorEntity jugadorEntity = competenciaJugadorService.getJugador(competenciaId);
		return modelMapper.map(jugadorEntity, JugadorDetailDTO.class);
	}

    @PutMapping(value = "/{competenciaId}/jugadores/{jugadorId}")
	@ResponseStatus(code = HttpStatus.OK)
	public JugadorDetailDTO replaceJugador(@PathVariable Long competenciaId, @PathVariable Long jugadorId)
			throws EntityNotFoundException {
		JugadorEntity jugadorEntity = competenciaJugadorService.replaceJugador(competenciaId, jugadorId);
		return modelMapper.map(jugadorEntity, JugadorDetailDTO.class);
	}

    @DeleteMapping(value = "/{competenciaId}/jugador")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable Long competenciaId) throws EntityNotFoundException {
		competenciaJugadorService.removeJugador(competenciaId);
	}
}
