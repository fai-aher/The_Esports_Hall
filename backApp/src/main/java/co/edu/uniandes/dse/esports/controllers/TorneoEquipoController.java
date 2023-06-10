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
import co.edu.uniandes.dse.esports.services.TorneoEquipoService;

/**
 * Clase que implementa el recurso "torneos/{id}/equipos".
 */
@RestController
@RequestMapping("/equipos")
public class TorneoEquipoController {
    @Autowired
	private TorneoEquipoService torneoEquipoService;

	@Autowired
	private ModelMapper modelMapper;

    /**
	 * Busca y devuelve el equipo con el ID recibido en la URL, relativo a un torneo.
	 *
	 * @param torneoId El ID del torneo del cual se busca la equipo
	 * @param equipoId   El ID del equipo que se busca
	 * @return {@link EquipoDetailDTO} - El equipo encontrado en el torneo.
	 */
	@GetMapping(value = "/{torneoId}/equipos/{equipoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public EquipoDetailDTO getEquipo(@PathVariable Long torneoId, @PathVariable Long equipoId)
			throws EntityNotFoundException, IllegalOperationException {
		EquipoEntity equipoEntity = torneoEquipoService.getEquipo(torneoId, equipoId);
		return modelMapper.map(equipoEntity, EquipoDetailDTO.class);
	}

    /**
	 * Busca y devuelve todos los equipos que existen en un torneo.
	 *
	 * @param torneosId El ID del torneo del cual se buscan los equipos
	 * @return JSONArray {@link EquipoDetailDTO} - Los equipos encontrados en el torneo.
	 *         Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{torneoId}/equipos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<EquipoDetailDTO> getEquipos(@PathVariable Long torneoId) throws EntityNotFoundException {
		List<EquipoEntity> equipoEntity = torneoEquipoService.getEquipos(torneoId);
		return modelMapper.map(equipoEntity, new TypeToken<List<EquipoDetailDTO>>() {
		}.getType());
	}
}
