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
import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Torneo - Jugador
 */
@DataJpaTest
@Transactional
@Import(TorneoJugadorService.class)
class TorneoJugadorServiceTest {
    private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private TorneoJugadorService torneoJugadorService;

	@Autowired
	private TestEntityManager entityManager;

	private List<JugadorEntity> jugadorList = new ArrayList<>();
	private List<TorneoEntity> torneoList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from TorneoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from JugadorEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			TorneoEntity torneo = factory.manufacturePojo(TorneoEntity.class);
			entityManager.persist(torneo);
			torneoList.add(torneo);
		}
		for (int i = 0; i < 3; i++) {
			JugadorEntity entity = factory.manufacturePojo(JugadorEntity.class);
			entityManager.persist(entity);
			jugadorList.add(entity);
			if (i == 0) {
				torneoList.get(i).setJugador(entity);
			}
		}
	}

	/**
	 * Prueba para asociar un Torneo existente a un Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddJugador() throws EntityNotFoundException {
		JugadorEntity entity = jugadorList.get(0);
		TorneoEntity torneoEntity = torneoList.get(1);
		JugadorEntity response = torneoJugadorService.addJugador(entity.getId(), torneoEntity.getId());

		assertNotNull(response);
		assertEquals(entity.getId(), response.getId());
	}

	/**
	 * Prueba para asociar un Torneo existente a un Jugador que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			TorneoEntity torneoEntity = torneoList.get(1);
			torneoJugadorService.addJugador(0L, torneoEntity.getId());
		});
	}

	/**
	 * Prueba para asociar un Torneo que no existe a un Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddJugadorInvalidTorneo() {
		assertThrows(EntityNotFoundException.class, () -> {
			JugadorEntity entity = jugadorList.get(0);
			torneoJugadorService.addJugador(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar un Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetJugador() throws EntityNotFoundException {
		TorneoEntity entity = torneoList.get(0);
		JugadorEntity resultEntity = torneoJugadorService.getJugador(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getJugador().getId(), resultEntity.getId());
	}

	/**
	 * Prueba para consultar un Jugador de un torneo que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetJugadorInvalidTorneo() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			torneoJugadorService.getJugador(0L);
		});
	}

	/**
	 * Prueba para consultar un Jugador que no tiene torneo.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetJugadorNotTorneo() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			TorneoEntity torneo = torneoList.get(1);
			torneoJugadorService.getJugador(torneo.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Torneos asociadas a una instancia de
	 * Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceJugador() throws EntityNotFoundException {
		JugadorEntity entity = jugadorList.get(0);
		torneoJugadorService.replaceJugador(torneoList.get(1).getId(), entity.getId());

		TorneoEntity torneo = entityManager.find(TorneoEntity.class, torneoList.get(1).getId());
		assertTrue(torneo.getJugador().equals(entity));
	}

	/**
	 * Prueba para remplazar las instancias de Torneos asociadas a una instancia de
	 * un Jugador que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			torneoJugadorService.replaceJugador(torneoList.get(1).getId(), 0L);
		});
	}

	/**
	 * Prueba para remplazar las instancias de Torneos que no existen asociadas a una
	 * instancia de Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceJugadorInvalidTorneo() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			JugadorEntity entity = jugadorList.get(0);
			torneoJugadorService.replaceJugador(0L, entity.getId());

		});
	}

    /**
     * Prueba para desasociar un Torneo existente de un Jugador existente.
     * 
     * @throws EntityNotFoundException
     *
     * @throws co.edu.uniandes.csw.esports.exceptions.BusinessLogicException
     */
    @Test
    void testRemoveTorneo() throws EntityNotFoundException {
		torneoJugadorService.removeJugador(torneoList.get(0).getId());
		TorneoEntity torneo = entityManager.find(TorneoEntity.class, torneoList.get(0).getId());
		assertNull(torneo.getJugador());
	}

    /**
     * Prueba para desasociar un Torneo que no existe de un Jugador existente.
     * 
     * @throws EntityNotFoundException
     *
     * @throws co.edu.uniandes.csw.esports.exceptions.BusinessLogicException
     */
    @Test
    void testRemoveInvalidTorneo(){
		assertThrows(EntityNotFoundException.class, ()->{
			torneoJugadorService.removeJugador(0L);
		});
	}

	/**
	 * Prueba para desasociar un Torneo existente de un Jugador existente.
	 */
	@Test
	void testRemoveJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			torneoJugadorService.removeJugador(torneoList.get(1).getId());
		});
	}

}
