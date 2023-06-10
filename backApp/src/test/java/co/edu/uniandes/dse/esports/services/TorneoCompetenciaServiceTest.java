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
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({TorneoCompetenciaService.class, TorneoService.class})
class TorneoCompetenciaServiceTest {
    @Autowired
    private TorneoCompetenciaService torneoCompetenciaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private TorneoEntity torneo = new TorneoEntity();
	private List<CompetenciaEntity> competenciaList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from TorneoEntity").executeUpdate();
	}

    /**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		torneo = factory.manufacturePojo(TorneoEntity.class);
		entityManager.persist(torneo);

		for (int i = 0; i < 3; i++) {
			CompetenciaEntity entity = factory.manufacturePojo(CompetenciaEntity.class);
			entityManager.persist(entity);
			entity.setTorneo(torneo);
			competenciaList.add(entity);
			torneo.getCompetencias().add(entity);	
		}
	}

    /**
	 * Prueba para consultar la lista de competencias de un torneo.
	 */
	@Test
	void testGetCompetencias() throws EntityNotFoundException {
		List<CompetenciaEntity> competenciaEntities = torneoCompetenciaService.getCompetencias(torneo.getId());

		assertEquals(competenciaList.size(), competenciaEntities.size());

		for (int i = 0; i < competenciaList.size(); i++) {
			assertTrue(competenciaEntities.contains(competenciaList.get(0)));
		}
	}

    /**
	 * Prueba para consultar la lista de competencias de un torneo que no existe.
	 */
	@Test
	void testGetCompetenciasInvalidTorneo(){
		assertThrows(EntityNotFoundException.class, ()->{
			torneoCompetenciaService.getCompetencias(0L);
		});
	}

    /**
	 * Prueba para consultar un competencia de un torneo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetCompetencia() throws EntityNotFoundException, IllegalOperationException {
		CompetenciaEntity competenciaEntity = competenciaList.get(0);
		CompetenciaEntity competencia = torneoCompetenciaService.getCompetencia(torneo.getId(), competenciaEntity.getId());
		assertNotNull(competencia);

		assertEquals(competenciaEntity.getId(), competencia.getId());
		assertEquals(competenciaEntity.getNombre(), competencia.getNombre());
		assertEquals(competenciaEntity.getDuracion(), competencia.getDuracion());
		assertEquals(competenciaEntity.getEquiposParticipantes(), competencia.getEquiposParticipantes());
		assertEquals(competenciaEntity.getResultadosFinales(), competencia.getResultadosFinales());
        assertEquals(competenciaEntity.getEquipoGanador(), competencia.getEquipoGanador());
        assertEquals(competenciaEntity.getTorneo(), competencia.getTorneo());
        assertEquals(competenciaEntity.getJugador(), competencia.getJugador());
        assertEquals(competenciaEntity.getMvp(), competencia.getMvp());
	}

    /**
	 * Prueba para consultar un competencia que no existe de un torneo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidCompetencia()  {
		assertThrows(EntityNotFoundException.class, ()->{
			torneoCompetenciaService.getCompetencia(torneo.getId(), 0L);
		});
	}

    /**
	 * Prueba para consultar un competencia de un torneo que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetCompetenciaInvalidTorneo() {
		assertThrows(EntityNotFoundException.class, ()->{
			CompetenciaEntity competenciaEntity = competenciaList.get(0);
			torneoCompetenciaService.getCompetencia(0L, competenciaEntity.getId());
		});
	}

    /**
	 * Prueba para obtener un competencia no asociado a un torneo.
	 *
	 */
	@Test
	void testGetNotAssociatedCompetencia() {
		assertThrows(IllegalOperationException.class, ()->{
			TorneoEntity newTorneo = factory.manufacturePojo(TorneoEntity.class);
			entityManager.persist(newTorneo);
			CompetenciaEntity competencia = factory.manufacturePojo(CompetenciaEntity.class);
			entityManager.persist(competencia);
			torneoCompetenciaService.getCompetencia(newTorneo.getId(), competencia.getId());
		});
	}
}
