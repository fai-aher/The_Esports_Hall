package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.entities.LogrosEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(LogrosService.class)
class LogrosServiceTest {
    

    @Autowired
	private LogrosService logrosService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<LogrosEntity> logroList = new ArrayList<>();
	private JugadorEntity jugadorEntity;

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from LogrosEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from JugadorEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {

		jugadorEntity = factory.manufacturePojo(JugadorEntity.class);
		entityManager.persist(jugadorEntity);
		
		for (int i = 0; i < 3; i++) {
			LogrosEntity entity = factory.manufacturePojo(LogrosEntity.class);
			entity.setJugador(jugadorEntity);
			entityManager.persist(entity);
			logroList.add(entity);
		}
		
		jugadorEntity.setLogrosObtenidos(logroList);
	}

	/**
	 * Prueba para crear un Logro.
	 */
	@Test
	void testCreateLogro() throws EntityNotFoundException {
		LogrosEntity newEntity = factory.manufacturePojo(LogrosEntity.class);
				
		LogrosEntity result = logrosService.createLogro(jugadorEntity.getId(), newEntity);
		assertNotNull(result);
		LogrosEntity entity = entityManager.find(LogrosEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
	}

	/**
	 * Prueba para crear un Logro con un jugador que no existe.
	 */
	@Test
	void testCreateLogroInvalidJugador() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			LogrosEntity newEntity = factory.manufacturePojo(LogrosEntity.class);
			logrosService.createLogro(0L, newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Logros.
	 */
	@Test
	void testGetLogros() throws EntityNotFoundException {
		List<LogrosEntity> list = logrosService.getLogros(jugadorEntity.getId());
		assertEquals(logroList.size(), list.size());
		for (LogrosEntity entity : list) {
			boolean found = false;
			for (LogrosEntity storedEntity : logroList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar la lista de Logros de un jugador que no existe.
	 */
	@Test
	void testGetLogrosInvalidJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			logrosService.getLogros(0L);
		});
	}

	/**
	 * Prueba para consultar un Logro.
	 */
	@Test
	void testGetLogro() throws EntityNotFoundException, IllegalOperationException {
		LogrosEntity entity = logroList.get(0);
		LogrosEntity resultEntity = logrosService.getLogro(jugadorEntity.getId(), entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
    }

	/**
	 * Prueba para consultar un Logro de un jugador que no existe.
	 */
	@Test
	void testGetLogroInvalidJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			LogrosEntity entity = logroList.get(0);
			logrosService.getLogro(0L, entity.getId());
		});
	}

	/**
	 * Prueba para consultar un Logro que no existe de un jugador.
	 */
	@Test
	void testGetInvalidLogro() {
		assertThrows(EntityNotFoundException.class, () -> {
			logrosService.getLogro(jugadorEntity.getId(), 0L);
		});
	}


	/**
	 * Prueba para actualizar un Logro.
	 */
	@Test
	void testUpdateLogro() throws EntityNotFoundException {
		LogrosEntity entity = logroList.get(0);
		LogrosEntity pojoEntity = factory.manufacturePojo(LogrosEntity.class);

		pojoEntity.setId(entity.getId());

		logrosService.updateLogro(jugadorEntity.getId(), entity.getId(), pojoEntity);

		LogrosEntity resp = entityManager.find(LogrosEntity.class, entity.getId());

		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getDescripcion(), resp.getDescripcion());
	}

	/**
	 * Prueba para actualizar un Logro de un jugador que no existe.
	 */
	@Test
	void testUpdateLogroInvalidJugador() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			LogrosEntity entity = logroList.get(0);
			LogrosEntity pojoEntity = factory.manufacturePojo(LogrosEntity.class);
			pojoEntity.setId(entity.getId());
			logrosService.updateLogro(0L, entity.getId(), pojoEntity);
		});
		
	}
	
	/**
	 * Prueba para actualizar un Logro que no existe de un jugador.
	 */
	@Test
	void testUpdateInvalidLogro(){
		assertThrows(EntityNotFoundException.class, ()->{
			LogrosEntity pojoEntity = factory.manufacturePojo(LogrosEntity.class);
			logrosService.updateLogro(jugadorEntity.getId(), 0L, pojoEntity);
		});
	}

	/**
     * Prueba para eliminar un Logro.
     */
	@Test
	void testDeleteLogro() throws EntityNotFoundException, IllegalOperationException {
		LogrosEntity entity = logroList.get(0);
		logrosService.deleteLogro(jugadorEntity.getId(), entity.getId());
		LogrosEntity deleted = entityManager.find(LogrosEntity.class, entity.getId());
		assertNull(deleted);
	}
	
	/**
     * Prueba para eliminar un Logro de un jugador que no existe.
     */
	@Test
	void testDeleteLogroInvalidJugador()  {
		assertThrows(EntityNotFoundException.class, ()->{
			LogrosEntity entity = logroList.get(0);
			logrosService.deleteLogro(0L, entity.getId());
		});
	}

	 /**
     * Prueba para eliminarle un logros a un jugador del cual no pertenece.
     */
	@Test
	void testDeleteInvalidLogro() {
		assertThrows(EntityNotFoundException.class, () -> {
			logrosService.deleteLogro(jugadorEntity.getId(), 0L);
		});
	}
}