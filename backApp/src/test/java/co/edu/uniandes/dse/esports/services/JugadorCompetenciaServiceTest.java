package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Jugador - Competencias
 */

@DataJpaTest
@Transactional
@Import({JugadorService.class, JugadorCompetenciaService.class})
class JugadorCompetenciaServiceTest {

    @Autowired
	private JugadorCompetenciaService jugadorCompetenciaService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<JugadorEntity> jugadoresList = new ArrayList<>();
	private List<CompetenciaEntity> competenciasList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from JugadorEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from CompetenciaEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			CompetenciaEntity competencia = factory.manufacturePojo(CompetenciaEntity.class);
			entityManager.persist(competencia);
			competenciasList.add(competencia);
		}

		for (int i = 0; i < 3; i++) {
			JugadorEntity entity = factory.manufacturePojo(JugadorEntity.class);
			entityManager.persist(entity);
			jugadoresList.add(entity);
			if (i == 0) {
				competenciasList.get(i).setJugador(entity);
				entity.getCompetenciasParticipadas().add(competenciasList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar una Competencia existente a un Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddCompetencia() throws EntityNotFoundException {
		JugadorEntity entity = jugadoresList.get(0);
		CompetenciaEntity competenciaEntity = competenciasList.get(1);
		CompetenciaEntity response = jugadorCompetenciaService.addCompetencia(competenciaEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(competenciaEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar una Competencia que no existe a un Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidCompetencia() {
		assertThrows(EntityNotFoundException.class, ()->{
			JugadorEntity entity = jugadoresList.get(0);
			jugadorCompetenciaService.addCompetencia(0L, entity.getId());
		});
	}
	
	/**
	 * Prueba para asociar una Competencia a un Jugador que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddCompetenciaInvalidJugador() {
		assertThrows(EntityNotFoundException.class, ()->{
			CompetenciaEntity competenciaEntity = competenciasList.get(1);
			jugadorCompetenciaService.addCompetencia(competenciaEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una colección de instancias de Competencias asociadas a una
	 * instancia Jugador.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testGetCompetencias() throws EntityNotFoundException {
		List<CompetenciaEntity> list = jugadorCompetenciaService.getCompetencias(jugadoresList.get(0).getId());
		assertEquals(1, list.size());
	}
	
	/**
	 * Prueba para obtener una colección de instancias de Competencias asociadas a una
	 * instancia Jugador que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testGetCompetenciasInvalidJugador() {
		assertThrows(EntityNotFoundException.class,()->{
			jugadorCompetenciaService.getCompetencias(0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Competencia asociada a una instancia Jugador.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.esports.exceptions.BusinessLogicException
	 */
	@Test
	void testGetCompetencia() throws EntityNotFoundException, IllegalOperationException {
		JugadorEntity entity = jugadoresList.get(0);
		CompetenciaEntity competenciaEntity = competenciasList.get(0);
		CompetenciaEntity response = jugadorCompetenciaService.getCompetencia(entity.getId(), competenciaEntity.getId());

		assertEquals(competenciaEntity.getId(), response.getId());
		assertEquals(competenciaEntity.getNombre(), response.getNombre());
		assertEquals(competenciaEntity.getDuracion(), response.getDuracion());
	}
	
	/**
	 * Prueba para obtener una instancia de Competencia asociada a una instancia Jugador que no existe.
	 * 
	 * @throws EntityNotFoundException
	 *
	 */
	@Test
	void testGetCompetenciaInvalidJugador()  {
		assertThrows(EntityNotFoundException.class, ()->{
			CompetenciaEntity competenciaEntity = competenciasList.get(0);
			jugadorCompetenciaService.getCompetencia(0L, competenciaEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener una instancia de Competencia que no existe asociada a una instancia Jugador.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidCompetencia()  {
		assertThrows(EntityNotFoundException.class, ()->{
			JugadorEntity entity = jugadoresList.get(0);
			jugadorCompetenciaService.getCompetencia(entity.getId(), 0L);
		});
	}

    /**
     * Prueba para obtener una instancia de Competencias asociada a una instancia Jugador
     * que no le pertenece.
     *
     * @throws co.edu.uniandes.csw.esports.exceptions.BusinessLogicException
     */
    @Test
    void getCompetenciaNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			JugadorEntity entity = jugadoresList.get(0);
			CompetenciaEntity competenciaEntity = competenciasList.get(1);
			jugadorCompetenciaService.getCompetencia(entity.getId(), competenciaEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Competencias asociadas a una instancia de
	 * Jugador.
	 */
	@Test
	void testReplaceCompetencias() throws EntityNotFoundException {
		JugadorEntity entity = jugadoresList.get(0);
		List<CompetenciaEntity> list = competenciasList.subList(1, 3);
		jugadorCompetenciaService.replaceCompetencias(entity.getId(), list);

		for (CompetenciaEntity book : list) {
			CompetenciaEntity c = entityManager.find(CompetenciaEntity.class, book.getId());
			assertTrue(c.getJugador().equals(entity));
		}
	}
	
	/**
	 * Prueba para remplazar las instancias de Competencias que no existen asociadas a una instancia de
	 * Jugador.
	 */
	@Test
	void testReplaceInvalidCompetencias() {
		assertThrows(EntityNotFoundException.class, ()->{
			JugadorEntity entity = jugadoresList.get(0);
			
			List<CompetenciaEntity> books = new ArrayList<>();
			CompetenciaEntity newBook = factory.manufacturePojo(CompetenciaEntity.class);
			newBook.setId(0L);
			books.add(newBook);
			
			jugadorCompetenciaService.replaceCompetencias(entity.getId(), books);
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Competencias asociadas a una instancia de
	 * Jugador que no existe.
	 */
	@Test
	void testReplaceCompetenciasInvalidJugador() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			List<CompetenciaEntity> list = competenciasList.subList(1, 3);
			jugadorCompetenciaService.replaceCompetencias(0L, list);
		});
	}
}