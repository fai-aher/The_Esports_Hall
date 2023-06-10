package co.edu.uniandes.dse.esports.services;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;


import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Torneo
 */

@DataJpaTest
@Transactional
@Import(TorneoService.class)
class TorneoServiceTest {
    
    @Autowired
	private TorneoService torneoService;

	@Autowired
	private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<TorneoEntity> torneoList = new ArrayList<>();
    private List<CompetenciaEntity> competenciaList = new ArrayList<>();
    private List<EquipoEntity> equipoList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from TorneoEntity");
        entityManager.getEntityManager().createQuery("delete from CompetenciaEntity");
        entityManager.getEntityManager().createQuery("delete from EquipoEntity");
        entityManager.getEntityManager().createQuery("delete from JugadorEntity");
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			CompetenciaEntity competenciaEntity = factory.manufacturePojo(CompetenciaEntity.class);
			entityManager.persist(competenciaEntity);
			competenciaList.add(competenciaEntity);
		}

        for (int i = 0; i < 3; i++) {
			EquipoEntity equipoEntity = factory.manufacturePojo(EquipoEntity.class);
			entityManager.persist(equipoEntity);
			equipoList.add(equipoEntity);
		}

        for (int i = 0; i < 3; i++) {
			TorneoEntity torneoEntity = factory.manufacturePojo(TorneoEntity.class);
			entityManager.persist(torneoEntity);
			torneoList.add(torneoEntity);
		}
	}

    /**
	 * Prueba para crear un Torneo
     * @throws ParseException
	 */
	@Test
	void testCreateTorneo() throws EntityNotFoundException, IllegalOperationException, ParseException {

		TorneoEntity newEntity = factory.manufacturePojo(TorneoEntity.class);
        newEntity.setCompetencias(competenciaList);
        newEntity.setEquiposParticipantes(equipoList);
		Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2000");  
		newEntity.setFechaFinalizacion(fecha);
		TorneoEntity result = torneoService.createTorneo(newEntity);
		assertNotNull(result);
		TorneoEntity entity = entityManager.find(TorneoEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getFechaFinalizacion(), entity.getFechaFinalizacion());
		assertEquals(newEntity.getPaisRealizacion(), entity.getPaisRealizacion());
		assertEquals(newEntity.getNombreTorneo(), entity.getNombreTorneo());
		assertEquals(newEntity.getImagenRepresentativa(), entity.getImagenRepresentativa());
		assertEquals(newEntity.getEnlacePaginaWeb(), entity.getEnlacePaginaWeb());
		assertEquals(newEntity.getOrganizador(), entity.getOrganizador());
        assertEquals(newEntity.getVideojuego(), entity.getVideojuego());
		assertEquals(entity.getEquiposParticipantes(), entity.getEquiposParticipantes());
		assertEquals(entity.getCompetencias(), entity.getCompetencias());
		assertEquals(entity.getJugador(), entity.getJugador());
	}

    /**
	 * Prueba para crear un Torneo con una lista de competencias inválida (null)
	 */
	@Test
	void testCreateTorneoWithNullCompetencias() {

		assertThrows(IllegalOperationException.class, () -> {
			TorneoEntity newEntity = factory.manufacturePojo(TorneoEntity.class);
			newEntity.setCompetencias(null);
			torneoService.createTorneo(newEntity);
		});
	}

    /**
	 * Prueba para crear un Torneo con una lista de equipos inválida (null)
	 */
	@Test
	void testCreateTorneoWithNullEquipos() {

		assertThrows(IllegalOperationException.class, () -> {
			TorneoEntity newEntity = factory.manufacturePojo(TorneoEntity.class);
			newEntity.setEquiposParticipantes(null);
			torneoService.createTorneo(newEntity);
		});
	}

    /**
	 * Prueba para crear un Torneo con una fecha de finalizacion invalida
	 */
	@Test
	void testCreateTorneoInvalidFecha() {

		assertThrows(IllegalOperationException.class, () -> {
			TorneoEntity newEntity = factory.manufacturePojo(TorneoEntity.class);
			Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2090");  
			newEntity.setFechaFinalizacion(fecha);
			torneoService.createTorneo(newEntity);
		});
	}

    /**
	 * Prueba para consultar la lista de Torneos.
	 */
	@Test
	void testGetTorneos() {
		List<TorneoEntity> list = torneoService.getTorneos();
		assertEquals(torneoList.size(), list.size());
		for (TorneoEntity entity : list) {
			boolean found = false;
			for (TorneoEntity storedEntity : torneoList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

    /**
	 * Prueba para consultar un Torneo.
	 */
	@Test
	void testGetTorneo() throws EntityNotFoundException {
		TorneoEntity entity = torneoList.get(0);
		TorneoEntity resultEntity = torneoService.getTorneo(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getFechaFinalizacion(), resultEntity.getFechaFinalizacion());
		assertEquals(entity.getPaisRealizacion(), resultEntity.getPaisRealizacion());
		assertEquals(entity.getNombreTorneo(), resultEntity.getNombreTorneo());
		assertEquals(entity.getImagenRepresentativa(), resultEntity.getImagenRepresentativa());
		assertEquals(entity.getEnlacePaginaWeb(), resultEntity.getEnlacePaginaWeb());
		assertEquals(entity.getOrganizador(), resultEntity.getOrganizador());
        assertEquals(entity.getVideojuego(), resultEntity.getVideojuego());
		assertEquals(entity.getEquiposParticipantes(), resultEntity.getEquiposParticipantes());
		assertEquals(entity.getCompetencias(), resultEntity.getCompetencias());
		assertEquals(entity.getJugador(), resultEntity.getJugador());
	}

    /**
	 * Prueba para consultar un Torneo que no existe.
	 */
	@Test
	void testGetInvalidTorneo() {
		assertThrows(EntityNotFoundException.class,()->{
			torneoService.getTorneo(0L);
		});
	}

    /**
	 * Prueba para actualizar un Torneo.
     * @throws ParseException
	 */
	@Test
	void testUpdateTorneo() throws EntityNotFoundException, IllegalOperationException, ParseException {
		TorneoEntity entity = torneoList.get(0);
		TorneoEntity pojoEntity = factory.manufacturePojo(TorneoEntity.class);
		pojoEntity.setId(entity.getId());
		Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2000");  
		pojoEntity.setFechaFinalizacion(fecha);
		torneoService.updateTorneo(entity.getId(), pojoEntity);

		TorneoEntity resp = entityManager.find(TorneoEntity.class, entity.getId());
		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(entity.getFechaFinalizacion(), resp.getFechaFinalizacion());
		assertEquals(entity.getPaisRealizacion(), resp.getPaisRealizacion());
		assertEquals(entity.getNombreTorneo(), resp.getNombreTorneo());
		assertEquals(entity.getImagenRepresentativa(), resp.getImagenRepresentativa());
		assertEquals(entity.getEnlacePaginaWeb(), resp.getEnlacePaginaWeb());
		assertEquals(entity.getOrganizador(), resp.getOrganizador());
        assertEquals(entity.getVideojuego(), resp.getVideojuego());
		assertEquals(entity.getEquiposParticipantes(), resp.getEquiposParticipantes());
		assertEquals(entity.getCompetencias(), resp.getCompetencias());
		assertEquals(entity.getJugador(), resp.getJugador());
	}

    /**
	 * Prueba para actualizar un Torneo inválido.
	 */
	@Test
	void testUpdateInvalidTorneo() {
		assertThrows(EntityNotFoundException.class, () -> {
			TorneoEntity pojoEntity = factory.manufacturePojo(TorneoEntity.class);
			pojoEntity.setId(0L);
			torneoService.updateTorneo(0L, pojoEntity);
		});
	}

    /**
	 * Prueba para actualizar un Torneo con una lista de competencias inválida (null).
	 */
	@Test
	void testUpdateTorneoWithNullCompetencias() {
		assertThrows(IllegalOperationException.class, () -> {
			TorneoEntity entity = torneoList.get(0);
			TorneoEntity pojoEntity = factory.manufacturePojo(TorneoEntity.class);
			pojoEntity.setCompetencias(null);
			pojoEntity.setId(entity.getId());
			torneoService.updateTorneo(entity.getId(), pojoEntity);
		});
	}

    /**
	 * Prueba para actualizar un Torneo con una lista de equipos inválida (null).
	 */
	@Test
	void testUpdateTorneoWithNullEquipos() {
		assertThrows(IllegalOperationException.class, () -> {
			TorneoEntity entity = torneoList.get(0);
			TorneoEntity pojoEntity = factory.manufacturePojo(TorneoEntity.class);
			pojoEntity.setEquiposParticipantes(null);
			pojoEntity.setId(entity.getId());
			torneoService.updateTorneo(entity.getId(), pojoEntity);
		});
	}

    /**
	 * Prueba para actualizar un Torneo con una fecha de finalizacion invalida.
	 */
	@Test
	void testUpdateTorneoInvalidAnio() {
		assertThrows(IllegalOperationException.class, () -> {
			TorneoEntity entity = torneoList.get(0);
			TorneoEntity pojoEntity = factory.manufacturePojo(TorneoEntity.class);
			Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2090");  
			pojoEntity.setFechaFinalizacion(fecha);
			pojoEntity.setId(entity.getId());
			torneoService.updateTorneo(entity.getId(), pojoEntity);
		});
	}

    /**
	 * Prueba para eliminar un Torneo.
	 */
	@Test
	void testDeleteTorneo() throws EntityNotFoundException, IllegalOperationException {
		TorneoEntity entity = torneoList.get(1);
		torneoService.deleteTorneo(entity.getId());
		TorneoEntity deleted = entityManager.find(TorneoEntity.class, entity.getId());
		assertNull(deleted);
	}

    /**
	 * Prueba para eliminar un Torneo que no existe.
	 */
	@Test
	void testDeleteInvalidTorneo() {
		assertThrows(EntityNotFoundException.class, ()->{
			torneoService.deleteTorneo(0L);
		});
	}
}
