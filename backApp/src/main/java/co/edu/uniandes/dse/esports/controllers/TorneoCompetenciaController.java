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

import co.edu.uniandes.dse.esports.dto.CompetenciaDetailDTO;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.services.TorneoCompetenciaService;

/**
 * Clase que implementa el recurso "torneos/{id}/competencias".
 */
@RestController
@RequestMapping("/torneos")
public class TorneoCompetenciaController {
    @Autowired
	private TorneoCompetenciaService torneoCompetenciaService;

	@Autowired
	private ModelMapper modelMapper;

    /**
	 * Busca y devuelve la competencia con el ID recibido en la URL, relativo a un torneo.
	 *
	 * @param torneoId El ID del torneo del cual se busca la competencia
	 * @param competenciaId   El ID de la competencia que se busca
	 * @return {@link CompetenciaDetailDTO} - La competencia encontrada en el torneo.
	 */
	@GetMapping(value = "/{torneoId}/competencias/{competenciaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public CompetenciaDetailDTO getCompetencia(@PathVariable Long torneoId, @PathVariable Long competenciaId)
			throws EntityNotFoundException, IllegalOperationException {
		CompetenciaEntity competenciaEntity = torneoCompetenciaService.getCompetencia(torneoId, competenciaId);
		return modelMapper.map(competenciaEntity, CompetenciaDetailDTO.class);
	}

    /**
	 * Busca y devuelve todas las competencias que existen en un torneo.
	 *
	 * @param torneosId El ID del torneo del cual se buscan los competencias
	 * @return JSONArray {@link CompetenciaDetailDTO} - Las competencias encontradas en el torneo.
	 *         Si no hay ninguna retorna una lista vacía.
	 */
	@GetMapping(value = "/{torneoId}/competencias")
	@ResponseStatus(code = HttpStatus.OK)
	public List<CompetenciaDetailDTO> getCompetencias(@PathVariable Long torneoId) throws EntityNotFoundException {
		List<CompetenciaEntity> competenciaEntity = torneoCompetenciaService.getCompetencias(torneoId);
		return modelMapper.map(competenciaEntity, new TypeToken<List<CompetenciaDetailDTO>>() {
		}.getType());
	}
}
