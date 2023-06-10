package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ JugadorService.class, JugadorEquipoService.class })

class JugadorEquipoServiceTest {
    
    @Autowired
	private TestEntityManager entityManager;

	@Autowired
	private JugadorEquipoService jugadorEquipoService;

	@Autowired
	private JugadorService jugadorService;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<EquipoEntity> equiposList = new ArrayList<>();
	private List<JugadorEntity> jugadoresList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from EquipoEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			JugadorEntity jugadores = factory.manufacturePojo(JugadorEntity.class);
			entityManager.persist(jugadores);
			jugadoresList.add(jugadores);
		}
		for (int i = 0; i < 3; i++) {
			EquipoEntity entity = factory.manufacturePojo(EquipoEntity.class);
			entityManager.persist(entity);
			equiposList.add(entity);
			if (i == 0) {
				jugadoresList.get(i).setEquipo(entity);
			}
		}
	}

	/**
	 * Prueba para remplazar las instancias de Jugadores asociadas a una instancia de
	 * Equipo.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceEquipo() throws EntityNotFoundException {
		JugadorEntity entity = jugadoresList.get(0);
		jugadorEquipoService.replaceEquipo(entity.getId(), equiposList.get(1).getId());
		entity = jugadorService.getJugador(entity.getId());
		assertEquals(entity.getEquipo(), equiposList.get(1));
	}
	
	/**
	 * Prueba para remplazar las instancias de Jugadores asociadas a una instancia de
	 * Equipo con un jugador que no existe
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceEquipoInvalidJugador() {
		assertThrows(EntityNotFoundException.class, ()->{
			jugadorEquipoService.replaceEquipo(0L, equiposList.get(1).getId());
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Jugadores asociadas a una instancia de
	 * Equipo que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidEquipo() {
		assertThrows(EntityNotFoundException.class, ()->{
			JugadorEntity entity = jugadoresList.get(0);
			jugadorEquipoService.replaceEquipo(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para desasociar un Jugador existente de un Equipo existente
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.esports.exceptions.BusinessLogicException
	 */
	@Test
	void testRemoveEquipo() throws EntityNotFoundException {
		jugadorEquipoService.removeEquipo(jugadoresList.get(0).getId());
		JugadorEntity response = jugadorService.getJugador(jugadoresList.get(0).getId());
		assertNull(response.getEquipo());
	}
	
	/**
	 * Prueba para desasociar un Jugador que no existe de un Equipo
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.esports.exceptions.BusinessLogicException
	 */
	@Test
	void testRemoveEquipoInvalidJugador() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			jugadorEquipoService.removeEquipo(0L);
		});
	}

}
