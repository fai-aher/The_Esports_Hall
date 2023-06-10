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
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Competencia - Jugador
 */
@DataJpaTest
@Transactional
@Import(CompetenciaJugadorService.class)
class CompetenciaJugadorServiceTest {

	private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private CompetenciaJugadorService competenciaJugadorService;

	@Autowired
	private TestEntityManager entityManager;

	private List<JugadorEntity> jugadorList = new ArrayList<>();
	private List<CompetenciaEntity> competenciaList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from CompetenciaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from JugadorEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			CompetenciaEntity competencia = factory.manufacturePojo(CompetenciaEntity.class);
			entityManager.persist(competencia);
			competenciaList.add(competencia);
		}
		for (int i = 0; i < 3; i++) {
			JugadorEntity entity = factory.manufacturePojo(JugadorEntity.class);
			entityManager.persist(entity);
			jugadorList.add(entity);
			if (i == 0) {
				competenciaList.get(i).setJugador(entity);
			}
		}
	}

	/**
	 * Prueba para asociar un Competencia existente a un Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddJugador() throws EntityNotFoundException {
		JugadorEntity entity = jugadorList.get(0);
		CompetenciaEntity competenciaEntity = competenciaList.get(1);
		JugadorEntity response = competenciaJugadorService.addJugador(entity.getId(), competenciaEntity.getId());

		assertNotNull(response);
		assertEquals(entity.getId(), response.getId());
	}

	/**
	 * Prueba para asociar un Competencia existente a un Jugador que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			CompetenciaEntity competenciaEntity = competenciaList.get(1);
			competenciaJugadorService.addJugador(0L, competenciaEntity.getId());
		});
	}

	/**
	 * Prueba para asociar un Competencia que no existe a un Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddJugadorInvalidCompetencia() {
		assertThrows(EntityNotFoundException.class, () -> {
			JugadorEntity entity = jugadorList.get(0);
			competenciaJugadorService.addJugador(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar un Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetJugador() throws EntityNotFoundException {
		CompetenciaEntity entity = competenciaList.get(0);
		JugadorEntity resultEntity = competenciaJugadorService.getJugador(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getJugador().getId(), resultEntity.getId());
	}

	/**
	 * Prueba para consultar un Jugador de un competencia que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetJugadorInvalidCompetencia() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			competenciaJugadorService.getJugador(0L);
		});
	}

	/**
	 * Prueba para consultar un Jugador que no tiene competencia.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetJugadorNotCompetencia() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			CompetenciaEntity competencia = competenciaList.get(1);
			competenciaJugadorService.getJugador(competencia.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Competencias asociadas a una instancia de
	 * Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceJugador() throws EntityNotFoundException {
		JugadorEntity entity = jugadorList.get(0);
		competenciaJugadorService.replaceJugador(competenciaList.get(1).getId(), entity.getId());

		CompetenciaEntity competencia = entityManager.find(CompetenciaEntity.class, competenciaList.get(1).getId());
		assertTrue(competencia.getJugador().equals(entity));
	}

	/**
	 * Prueba para remplazar las instancias de Competencias asociadas a una instancia de
	 * un Jugador que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			competenciaJugadorService.replaceJugador(competenciaList.get(1).getId(), 0L);
		});
	}

	/**
	 * Prueba para remplazar las instancias de Competencias que no existen asociadas a una
	 * instancia de Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceJugadorInvalidCompetencia() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			JugadorEntity entity = jugadorList.get(0);
			competenciaJugadorService.replaceJugador(0L, entity.getId());

		});
	}

    /**
     * Prueba para desasociar un Competencia existente de un Jugador existente.
     * 
     * @throws EntityNotFoundException
     *
     * @throws co.edu.uniandes.csw.esports.exceptions.BusinessLogicException
     */
    @Test
    void testRemoveCompetencia() throws EntityNotFoundException {
		competenciaJugadorService.removeJugador(competenciaList.get(0).getId());
		CompetenciaEntity competencia = entityManager.find(CompetenciaEntity.class, competenciaList.get(0).getId());
		assertNull(competencia.getJugador());
	}

    /**
     * Prueba para desasociar un Competencia que no existe de un Jugador existente.
     * 
     * @throws EntityNotFoundException
     *
     * @throws co.edu.uniandes.csw.esports.exceptions.BusinessLogicException
     */
    @Test
    void testRemoveInvalidCompetencia(){
		assertThrows(EntityNotFoundException.class, ()->{
			competenciaJugadorService.removeJugador(0L);
		});
	}

	/**
	 * Prueba para desasociar un Competencia existente de un Jugador existente.
	 */
	@Test
	void testRemoveJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			competenciaJugadorService.removeJugador(competenciaList.get(1).getId());
		});
	}

}
