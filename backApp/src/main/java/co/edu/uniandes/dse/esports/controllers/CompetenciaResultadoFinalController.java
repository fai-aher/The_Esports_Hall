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

import co.edu.uniandes.dse.esports.dto.ResultadoFinalDTO;
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.services.CompetenciaResultadoFinalService;

@RestController
@RequestMapping("/competencias")
public class CompetenciaResultadoFinalController {

    @Autowired
	private CompetenciaResultadoFinalService competenciaResultadoFinalService;

	@Autowired
	private ModelMapper modelMapper;

    @GetMapping(value = "/{competenciaId}/resultadosFinales")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ResultadoFinalDTO> getResultadosFinales(@PathVariable Long competenciaId) throws EntityNotFoundException {
		List<ResultadoFinalEntity> resultadoFinalEntity = competenciaResultadoFinalService.getResultadosFinales(competenciaId);
		return modelMapper.map(resultadoFinalEntity, new TypeToken<List<ResultadoFinalDTO>>() {
		}.getType());
	}
    
}
