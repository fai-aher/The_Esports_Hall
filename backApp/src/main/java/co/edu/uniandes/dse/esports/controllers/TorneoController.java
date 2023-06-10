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

import co.edu.uniandes.dse.esports.dto.TorneoDTO;
import co.edu.uniandes.dse.esports.dto.TorneoDetailDTO;
import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.services.TorneoService;


// Clase que implementa el recurso "torneos"

@RestController
@RequestMapping("/torneos")
public class TorneoController {
    @Autowired
    private TorneoService torneoService;

    @Autowired
    private ModelMapper modelMapper;

    /**
	 * Busca y devuelve todos los torneos que existen en la aplicacion.
	 *
	 * @return JSONArray {@link TorneoDetailDTO} - Los torneos encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<TorneoDetailDTO> findAll() {
		List<TorneoEntity> torneos = torneoService.getTorneos();
		return modelMapper.map(torneos, new TypeToken<List<TorneoDetailDTO>>() {
		}.getType());
	}

    /**
	 * Busca el torneo con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param torneoId Identificador del torneo que se esta buscando. Este debe ser una
	 *               cadena de dígitos.
	 * @return JSON {@link TorneoDetailDTO} - El torneo buscado
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public TorneoDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		TorneoEntity torneoEntity = torneoService.getTorneo(id);
		return modelMapper.map(torneoEntity, TorneoDetailDTO.class);
	}

    /**
	 * Crea un nuevo torneo con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param torneo {@link TorneoDTO} - EL torneo que se desea guardar.
	 * @return JSON {@link TorneoDTO} - El torneo guardado con el atributo id
	 *         autogenerado.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public TorneoDTO create(@RequestBody TorneoDTO torneoDTO) throws IllegalOperationException, EntityNotFoundException {
		TorneoEntity torneoEntity = torneoService.createTorneo(modelMapper.map(torneoDTO, TorneoEntity.class));
		return modelMapper.map(torneoEntity, TorneoDTO.class);
	}

    /**
	 * Actualiza el torneo con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param torneoId Identificador del torneo que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param torneo   {@link TorneoDTO} El torneo que se desea guardar.
	 * @return JSON {@link TorneoDTO} - El torneo guardada.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public TorneoDTO update(@PathVariable Long id, @RequestBody TorneoDTO torneoDTO)
			throws EntityNotFoundException, IllegalOperationException {
		TorneoEntity torneoEntity = torneoService.updateTorneo(id, modelMapper.map(torneoDTO, TorneoEntity.class));
		return modelMapper.map(torneoEntity, TorneoDTO.class);
	}

    /**
	 * Borra el torneo con el id asociado recibido en la URL.
	 *
	 * @param torneoId Identificador del torneo que se desea borrar. Este debe ser una
	 *               cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
		torneoService.deleteTorneo(id);
	}
}
