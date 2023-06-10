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

import co.edu.uniandes.dse.esports.dto.JugadorDTO;
import co.edu.uniandes.dse.esports.dto.JugadorDetailDTO;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.services.JugadorService;

/**
 * Clase que implementa el recurso "jugadores".
 */

@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private ModelMapper modelMapper;
    
    /**
	 * Busca y devuelve todos los jugadores que existen en la aplicacion.
	 *
	 * @return JSONArray {@link JugadorDetailDTO} - Los jugadores encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<JugadorDetailDTO> findAll() {
		List<JugadorEntity> jugadores = jugadorService.getAllJugadores();
		return modelMapper.map(jugadores, new TypeToken<List<JugadorDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el jugador con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param id Identificador del jugador que se esta buscando. Este debe ser una
	 *           cadena de dígitos.
	 * @return JSON {@link JugadorDetailDTO} - El jugador buscado
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public JugadorDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		JugadorEntity authorEntity = jugadorService.getJugador(id);
		return modelMapper.map(authorEntity, JugadorDetailDTO.class);
	}

	/**
	 * Crea un nuevo jugador con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param jugadorDTO {@link JugadorDTO} - EL jugador que se desea guardar.
	 * @return JSON {@link JugadorDTO} - El jugador guardado con el atributo id
	 *         autogenerado.
	 * @throws IllegalOperationException 
	 * @throws EntityNotFoundException
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public JugadorDTO create(@RequestBody JugadorDTO jugadorDTO) throws IllegalOperationException, EntityNotFoundException {
		JugadorEntity jugadorEntity = jugadorService.createJugador(modelMapper.map(jugadorDTO, JugadorEntity.class));
		return modelMapper.map(jugadorEntity, JugadorDTO.class);
	}

	/**
	 * Actualiza el jugador con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param id     Identificador del jugador que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param jugador {@link JugadorDTO} El jugador que se desea guardar.
	 * @throws IllegalOperationException
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public JugadorDTO update(@PathVariable Long id, @RequestBody JugadorDTO jugadorDTO)
			throws EntityNotFoundException, IllegalOperationException {
		JugadorEntity authorEntity = jugadorService.updateJugador(id, modelMapper.map(jugadorDTO, JugadorEntity.class));
		return modelMapper.map(authorEntity, JugadorDTO.class);
	}

	/**
	 * Borra el jugador con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del jugador que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
		jugadorService.deleteJugador(id);
	}
}
