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

import co.edu.uniandes.dse.esports.dto.LogrosDTO;
import co.edu.uniandes.dse.esports.entities.LogrosEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.services.LogrosService;

/**
 * Clase que implementa el recurso "logros".
 */
@RestController
@RequestMapping("/jugadores")
public class LogrosController {

	@Autowired
	private LogrosService logrosService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Crea una nueva logros con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param jugadorId El ID del jugador del cual se le agrega la logros
	 * @param logro {@link LogrosDTO} - La logros que se desea guardar.
	 * @return JSON {@link LogrosDTO} - La logros guardada con el atributo id
	 *         autogenerado.
	 */
	@PostMapping(value = "/{jugadorId}/logros")
	@ResponseStatus(code = HttpStatus.CREATED)
	public LogrosDTO createReview(@PathVariable Long jugadorId, @RequestBody LogrosDTO logro)
			throws EntityNotFoundException {
		LogrosEntity reviewEnity = modelMapper.map(logro, LogrosEntity.class);
		LogrosEntity newReview = logrosService.createLogro(jugadorId, reviewEnity);
		return modelMapper.map(newReview, LogrosDTO.class);
	}

	/**
	 * Busca y devuelve todas las logros que existen en un jugador.
	 *
	 * @param jugadorId El ID del jugador del cual se buscan las logros
	 * @return JSONArray {@link LogrosDTO} - Las logros encontradas en el jugador. Si
	 *         no hay ninguna retorna una lista vacía.
	 */
	@GetMapping(value = "/{jugadorId}/logros")
	@ResponseStatus(code = HttpStatus.OK)
	public List<LogrosDTO> getLogros(@PathVariable Long jugadorId) throws EntityNotFoundException {
		List<LogrosEntity> logros = logrosService.getLogros(jugadorId);
		return modelMapper.map(logros, new TypeToken<List<LogrosDTO>>() {
		}.getType());
	}

	/**
	 * Busca y devuelve la logros con el ID recibido en la URL, relativa a un jugador.
	 *
	 * @param jugadorId   El ID del jugador del cual se buscan las logros
	 * @param logrosId El ID de la logros que se busca
	 * @return {@link LogrosDTO} - La logros encontradas en el jugador.
	 * @throws IllegalOperationException
	 */
	@GetMapping(value = "/{jugadorId}/logros/{logrosId}")
	@ResponseStatus(code = HttpStatus.OK)
	public LogrosDTO getLogro(@PathVariable Long jugadorId, @PathVariable Long logrosId)
			throws EntityNotFoundException, IllegalOperationException {
		LogrosEntity entity = logrosService.getLogro(jugadorId, logrosId);
		return modelMapper.map(entity, LogrosDTO.class);
	}

	/**
	 * Actualiza una logros con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa el objeto actualizado.
	 *
	 * @param jugadorId   El ID del jugador del cual se guarda la logros
	 * @param logrosId El ID de la logros que se va a actualizar
	 * @param logro   {@link LogrosDTO} - La logros que se desea guardar.
	 * @return JSON {@link LogrosDTO} - La logros actualizada.
	 */
	@PutMapping(value = "/{jugadorId}/logros/{reviewsId}")
	@ResponseStatus(code = HttpStatus.OK)
	public LogrosDTO updateLogro(@PathVariable Long jugadorId, @PathVariable("reviewsId") Long logrosId,
			@RequestBody LogrosDTO logro) throws EntityNotFoundException {
		LogrosEntity reviewEntity = modelMapper.map(logro, LogrosEntity.class);
		LogrosEntity newEntity = logrosService.updateLogro(jugadorId, logrosId, reviewEntity);
		return modelMapper.map(newEntity, LogrosDTO.class);
	}

	/**
     * Borra la logros con el id asociado recibido en la URL.
     *
     * @param jugadorId El ID del jugador del cual se va a eliminar la logros.
     * @param logrosId El ID de la logros que se va a eliminar.
	 * @throws IllegalOperationException
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     */
	@DeleteMapping(value = "/{jugadorId}/logros/{logrosId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteLogro(@PathVariable Long jugadorId, @PathVariable Long logrosId)
			throws EntityNotFoundException, IllegalOperationException {
		logrosService.deleteLogro(jugadorId, logrosId);
	}
}
