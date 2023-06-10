package co.edu.uniandes.dse.esports.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.esports.dto.TorneoDetailDTO;
import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.services.CompetenciaTorneoService;


@RestController
@RequestMapping("/competencias")
public class CompetenciaTorneoController {

    @Autowired
	private CompetenciaTorneoService competenciaTorneoService;

	@Autowired
	private ModelMapper modelMapper;
    
    @PostMapping(value = "/{competenciaId}/torneos/{torneoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public TorneoDetailDTO addTorneo(@PathVariable Long torneoId, @PathVariable Long competenciaId)
			throws EntityNotFoundException {
		TorneoEntity torneoEntity = competenciaTorneoService.addTorneoCompetencia(competenciaId, torneoId);
		return modelMapper.map(torneoEntity, TorneoDetailDTO.class);
    }
    
    @GetMapping(value = "/{competenciaId}/torneo")
    @ResponseStatus(code = HttpStatus.OK)
    public TorneoDetailDTO getTorneo(@PathVariable Long competenciaId)
            throws EntityNotFoundException { 
        TorneoEntity torneoEntity = competenciaTorneoService.getTorneoCompetencia(competenciaId);
        return modelMapper.map(torneoEntity, TorneoDetailDTO.class);
    }

    @DeleteMapping(value = "/{competenciaId}/torneos/{torneoId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeTorneo(@PathVariable Long competenciaId, @PathVariable Long torneoId)
			throws EntityNotFoundException {
		competenciaTorneoService.removeTorneoCompetencia(torneoId, competenciaId);
	}

}
