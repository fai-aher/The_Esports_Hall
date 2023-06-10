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

import co.edu.uniandes.dse.esports.dto.EquipoDTO;
import co.edu.uniandes.dse.esports.dto.EquipoDetailDTO;
import co.edu.uniandes.dse.esports.services.EquipoService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;


@RestController
@RequestMapping("/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private ModelMapper modelMapper;

    /**
	 * Busca y devuelve todos los equipos que existen en la aplicacion.
	 *
	 * @return JSONArray {@link EquipoDetailDTO} - Los equipos encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<EquipoDetailDTO> findAll() {
		List<EquipoEntity> equipos = equipoService.getAllEquipos();
		return modelMapper.map(equipos, new TypeToken<List<EquipoDetailDTO>>() {
		}.getType());
	}

    /**
    * Busca el equipo con el id asociado recibido en la URL y lo devuelve.
    *
    * @param id Identificador del equipo que se está buscando. Este debe ser una cadena de dígitos.
    * @return JSON {@link EquipoDetailDTO} - El equipo buscado
    */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EquipoDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        EquipoEntity equipoEntity = equipoService.getEquipo(id);
        return modelMapper.map(equipoEntity, EquipoDetailDTO.class);
    }

    /**
    * Crea un nuevo equipo con la informacion que se recibe en el cuerpo de la
    * petición y se regresa un objeto identico con un id auto-generado por la base
    * de datos.
    *
    * @param equipoDTO {@link EquipoDTO} - El equipo que se desea guardar.
    * @return JSON {@link EquipoDTO} - El equipo guardado con el atributo id autogenerado.
    * @throws IllegalOperationException 
    * @throws EntityNotFoundException
    */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EquipoDTO create(@RequestBody EquipoDTO equipoDTO) throws IllegalOperationException, EntityNotFoundException {
        EquipoEntity equipoEntity = equipoService.createEquipo(modelMapper.map(equipoDTO, EquipoEntity.class));
        return modelMapper.map(equipoEntity, EquipoDTO.class);
        }       

    /**
    * Actualiza el equipo con el id recibido en la URL con la información que se
    * recibe en el cuerpo de la petición.
    *
    * @param id     Identificador del equipo que se desea actualizar. Este debe ser
    *               una cadena de dígitos.
    * @param equipo {@link EquipoDTO} El equipo que se desea guardar.
    * @throws IllegalOperationException
    */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EquipoDTO update(@PathVariable Long id, @RequestBody EquipoDetailDTO equipoDTO)
        throws EntityNotFoundException, IllegalOperationException {
        EquipoEntity equipoEntity = equipoService.updateEquipo(id, modelMapper.map(equipoDTO, EquipoEntity.class));
        return modelMapper.map(equipoEntity, EquipoDTO.class);
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
		equipoService.deleteEquipo(id);
	}

    
}

