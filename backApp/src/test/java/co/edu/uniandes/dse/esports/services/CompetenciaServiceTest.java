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

import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Competencia
 */

@DataJpaTest
@Transactional
@Import(CompetenciaService.class)
class CompetenciaServiceTest {
    
    @Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private TorneoEntity torneoEntity;
    private List<CompetenciaEntity> competenciaList = new ArrayList<>();
    private List<EquipoEntity> equipoList = new ArrayList<>();
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
			EquipoEntity equipoEntity = factory.manufacturePojo(EquipoEntity.class);
			entityManager.persist(equipoEntity);
			equipoList.add(equipoEntity);
		}

        torneoEntity = factory.manufacturePojo(TorneoEntity.class);
		entityManager.persist(torneoEntity);

		jugadorEntity = factory.manufacturePojo(JugadorEntity.class);
		entityManager.persist(jugadorEntity);

		for (int i = 0; i < 3; i++) {
			CompetenciaEntity competenciaEntity = factory.manufacturePojo(CompetenciaEntity.class);
			competenciaEntity.setEquiposParticipantes(equipoList);
			competenciaEntity.setTorneo(torneoEntity);
			competenciaEntity.setMvp(jugadorEntity);
			entityManager.persist(competenciaEntity);
			competenciaList.add(competenciaEntity);
		}
	}

    /**
	 * Prueba para crear un Competencia.
	 */
	@Test
	void testCreateCompetencia() throws EntityNotFoundException, IllegalOperationException {
		CompetenciaEntity newEntity = factory.manufacturePojo(CompetenciaEntity.class);
				
		CompetenciaEntity result = competenciaService.createCompetencia(newEntity);
		assertNotNull(result);
		CompetenciaEntity entity = entityManager.find(CompetenciaEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
		assertEquals(newEntity.getDuracion(), entity.getDuracion());
	}

	@Test
	void testCreateCompetenciaWithNullEquipos() 
	{
		assertThrows(IllegalOperationException.class, () -> {
			CompetenciaEntity newEntity = factory.manufacturePojo(CompetenciaEntity.class);
			newEntity.setEquiposParticipantes(null);
			competenciaService.createCompetencia(newEntity);
		});
	}

	@Test
	void testCreateTorneoWithNullDuracion() 
	{
		assertThrows(IllegalOperationException.class, () -> {
			CompetenciaEntity newEntity = factory.manufacturePojo(CompetenciaEntity.class);
			newEntity.setDuracion(null);
			competenciaService.createCompetencia(newEntity);
		});
	}

	@Test
	void testGetCompetencias() 
	{
		List<CompetenciaEntity> list = competenciaService.getCompetencias();
		assertEquals(competenciaList.size(), list.size());
		for (CompetenciaEntity entity : list) {
			boolean found = false;
			for (CompetenciaEntity storedEntity : competenciaList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	@Test
	void testGetCompetenciasMvp() throws EntityNotFoundException {
		List<CompetenciaEntity> list = competenciaService.getCompetenciasMvp(jugadorEntity.getId());
		for (CompetenciaEntity entity : list) {
			boolean found = false;
			for (CompetenciaEntity storedEntity : competenciaList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	@Test
	void testGetCompetenciasMvpInvalidJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			competenciaService.getCompetenciasMvp(0L);
		});
	}

	@Test
	void testGetCompetencia() throws EntityNotFoundException 
	{
		CompetenciaEntity entity = competenciaList.get(0);
		CompetenciaEntity resultEntity = competenciaService.getCompetencia(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getNombre(), resultEntity.getNombre());
		assertEquals(entity.getDuracion(), resultEntity.getDuracion());
	}

	@Test
	void testGetInvalidCompetencia() 
	{
		assertThrows(EntityNotFoundException.class,()->{
			competenciaService.getCompetencia(0L);
		});
	}

	@Test
	void testGetCompetenciaMvpInvalidJugador() {
		assertThrows(EntityNotFoundException.class, () -> {
			CompetenciaEntity entity = competenciaList.get(0);
			competenciaService.getCompetenciaMvp(0L, entity.getId());
		});
	}

	@Test
	void testUpdateCompetencia() throws EntityNotFoundException, IllegalOperationException {
		CompetenciaEntity entity = competenciaList.get(0);
		CompetenciaEntity pojoEntity = factory.manufacturePojo(CompetenciaEntity.class);

		pojoEntity.setId(entity.getId());

		competenciaService.updateCompetencia(entity.getId(), pojoEntity);

		CompetenciaEntity resp = entityManager.find(CompetenciaEntity.class, entity.getId());

		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getNombre(), resp.getNombre());
		assertEquals(pojoEntity.getDuracion(), resp.getDuracion());
	}
		
	
	@Test
	void testUpdateInvalidCompetencia(){
		assertThrows(EntityNotFoundException.class, ()->{
			CompetenciaEntity pojoEntity = factory.manufacturePojo(CompetenciaEntity.class);
			competenciaService.updateCompetencia( 0L, pojoEntity);
		});
	}

	@Test
	void testUpdateCompetenciaWithNullEquiposParticipantes() {
		assertThrows(IllegalOperationException.class, () -> {
			CompetenciaEntity entity = competenciaList.get(0);
			CompetenciaEntity pojoEntity = factory.manufacturePojo(CompetenciaEntity.class);
			pojoEntity.setEquiposParticipantes(null);
			pojoEntity.setId(entity.getId());
			competenciaService.updateCompetencia(entity.getId(), pojoEntity);
		});
	}

	@Test
	void testUpdateCompetenciaInvalidDuracion() {
		assertThrows(IllegalOperationException.class, () -> {
			CompetenciaEntity entity = competenciaList.get(0);
			CompetenciaEntity pojoEntity = factory.manufacturePojo(CompetenciaEntity.class);
			pojoEntity.setDuracion(null);
			pojoEntity.setId(entity.getId());
			competenciaService.updateCompetencia(entity.getId(), pojoEntity);
		});
	}

	@Test
	void testDeleteCompetencia() throws EntityNotFoundException, IllegalOperationException {
		CompetenciaEntity entity = competenciaList.get(1);
		competenciaService.deleteCompetencia(entity.getId());
		CompetenciaEntity deleted = entityManager.find(CompetenciaEntity.class, entity.getId());
		assertNull(deleted);
	}

	@Test
	void testDeleteInvalidCompetencia() {
		assertThrows(EntityNotFoundException.class, ()->{
			competenciaService.deleteCompetencia(0L);
		});
	}
}