package co.edu.uniandes.dse.esports.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.esports.dto.CompetenciaDTO;
import co.edu.uniandes.dse.esports.dto.CompetenciaDetailDTO;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.services.CompetenciaService;


@RestController
@RequestMapping("/competencias")
public class CompetenciaController {
    @Autowired
    private CompetenciaService competenciaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<CompetenciaDetailDTO> findAll() {
		List<CompetenciaEntity> competencias = competenciaService.getCompetencias();
		return modelMapper.map(competencias, new TypeToken<List<CompetenciaDetailDTO>>() {
		}.getType());
	}

    @GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public CompetenciaDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		CompetenciaEntity competenciaEntity = competenciaService.getCompetencia(id);
		return modelMapper.map(competenciaEntity, CompetenciaDetailDTO.class);
	}

    /**
    * Crea una nueva competencia con la informacion que se recibe en el cuerpo de la
    * petición y se regresa un objeto identico con un id auto-generado por la base
    * de datos.
    *
    * @param competenciaDTO {@link CompetenciaDTO} - La competencia que se desea guardar.
    * @return JSON {@link CompetenciaDTO} - La competencia guardada con el atributo id autogenerado.
    * @throws IllegalOperationException 
    * @throws EntityNotFoundException
    */

    @PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public CompetenciaDTO create(@RequestBody CompetenciaDTO competenciaDTO) throws IllegalOperationException, EntityNotFoundException {
		CompetenciaEntity competenciaEntity = competenciaService.createCompetencia((modelMapper.map(competenciaDTO, CompetenciaEntity.class)));
		return modelMapper.map(competenciaEntity, CompetenciaDTO.class);
	}

    @PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public CompetenciaDTO update(@PathVariable Long id, @RequestBody CompetenciaDTO competenciaDTO)
			throws EntityNotFoundException, IllegalOperationException {
		CompetenciaEntity competenciaEntity = competenciaService.updateCompetencia( id, modelMapper.map(competenciaDTO, CompetenciaEntity.class));
		return modelMapper.map(competenciaEntity, CompetenciaDTO.class);
	}

    @DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException {
		competenciaService.deleteCompetencia(id);
	}
    
}