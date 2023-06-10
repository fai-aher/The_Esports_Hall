package co.edu.uniandes.dse.esports.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.esports.dto.EquipoDetailDTO;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.services.CompetenciaEquipoService;

@RestController
@RequestMapping("/competencias")
public class CompetenciaEquipoController {

    @Autowired
	private CompetenciaEquipoService competenciaEquipoService;

	@Autowired
	private ModelMapper modelMapper;

    @GetMapping(value = "/{competenciaId}/equipos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<EquipoDetailDTO> getEquiposParticipantes(@PathVariable Long competenciaId) throws EntityNotFoundException {
		List<EquipoEntity> equipoEntity = competenciaEquipoService.getEquiposParticipantes(competenciaId);
		return modelMapper.map(equipoEntity, new TypeToken<List<EquipoDetailDTO>>() {
		}.getType());
	}

    @GetMapping(value = "/{competenciaId}/equipos/{equipoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public EquipoDetailDTO getEquipoParticipante(@PathVariable Long competenciaId, @PathVariable Long equipoId)
			throws EntityNotFoundException, IllegalOperationException {
		EquipoEntity equipoEntity = competenciaEquipoService.getEquipoParticipante(competenciaId, equipoId);
		return modelMapper.map(equipoEntity, EquipoDetailDTO.class);
	}


    
}
