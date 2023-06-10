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

import co.edu.uniandes.dse.esports.dto.ResultadoFinalDTO;
import co.edu.uniandes.dse.esports.services.ResultadoFinalService;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;


@RestController
@RequestMapping("/resultados")
public class ResultadoFinalController {

    @Autowired
    private ResultadoFinalService resultadoFinalService;

    @Autowired
    private ModelMapper modelMapper;


    /**
	 * Busca y devuelve todos los resultados que existen en la aplicacion.
	 *
	 * @return JSONArray {@link ResultadoFinalDTO} - Los resultados encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ResultadoFinalDTO> findAll() {
		List<ResultadoFinalEntity> equipos = resultadoFinalService.getAllResultadosFinales();
		return modelMapper.map(equipos, new TypeToken<List<ResultadoFinalDTO>>() {
		}.getType());
	}

    /**
     * Busca el resultado final con el id asociado recibido en la URL y lo devuelve.
     *
     * @param id Identificador del resultado final que se esta buscando. Este debe ser una
     *           cadena de dígitos.
     * @return JSON {@link ResultadoFinalDTO} - El resultado final buscado
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResultadoFinalDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        ResultadoFinalEntity resultadoFinalEntity = resultadoFinalService.getResultadoFinal(id);
        return modelMapper.map(resultadoFinalEntity, ResultadoFinalDTO.class);
    }

    /**
     * Crea un nuevo resultado final con la informacion que se recibe en el cuerpo de la
     * petición y se regresa un objeto identico con un id auto-generado por la base
     * de datos.
     *
     * @param resultadoFinalDTO {@link ResultadoFinalDTO} - EL resultado final que se desea guardar.
     * @return JSON {@link ResultadoFinalDTO} - El resultado final guardado con el atributo id
     *         autogenerado.
     * @throws IllegalOperationException 
     * @throws EntityNotFoundException
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResultadoFinalDTO create(@RequestBody ResultadoFinalDTO resultadoFinalDTO) throws IllegalOperationException, EntityNotFoundException {
        ResultadoFinalEntity resultadoFinalEntity = resultadoFinalService.createResultadoFinal(modelMapper.map(resultadoFinalDTO, ResultadoFinalEntity.class));
        return modelMapper.map(resultadoFinalEntity, ResultadoFinalDTO.class);
    }

    /**
     * Actualiza el resultado final con el id recibido en la URL con la información que se
     * recibe en el cuerpo de la petición.
     *
     * @param id     Identificador del resultado final que se desea actualizar. Este debe ser
     *               una cadena de dígitos.
     * @param resultadoFinal {@link ResultadoFinalDTO} El resultado final que se desea guardar.
     * @throws IllegalOperationException
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResultadoFinalDTO update(@PathVariable Long id, @RequestBody ResultadoFinalDTO resultadoFinalDTO)
            throws EntityNotFoundException, IllegalOperationException {
        ResultadoFinalEntity resultadoFinalEntity = resultadoFinalService.updateResultadoFinal(id, modelMapper.map(resultadoFinalDTO, ResultadoFinalEntity.class));
        return modelMapper.map(resultadoFinalEntity, ResultadoFinalDTO.class);
    }

	/**
	 * Borra el resultado final con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del resultado que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
		resultadoFinalService.deleteResultadoFinal(id);
	}

}
