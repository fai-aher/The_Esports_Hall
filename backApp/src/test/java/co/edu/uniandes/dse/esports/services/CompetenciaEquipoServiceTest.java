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
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({CompetenciaEquipoService.class, CompetenciaService.class})
class CompetenciaEquipoServiceTest {
    @Autowired
    private CompetenciaEquipoService competenciaEquipoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CompetenciaEntity competencia = new CompetenciaEntity();
	private List<EquipoEntity> equipoList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    /**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from EquipoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from CompetenciaEntity").executeUpdate();
	}

    /**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		competencia = factory.manufacturePojo(CompetenciaEntity.class);
		entityManager.persist(competencia);

		for (int i = 0; i < 3; i++) {
			EquipoEntity entity = factory.manufacturePojo(EquipoEntity.class);
			entityManager.persist(entity);
			entity.getCompetenciasParticipadas().add(competencia);
			equipoList.add(entity);
			competencia.getEquiposParticipantes().add(entity);	
		}
	}

    /**
	 * Prueba para consultar la lista de equipos participantes de una competenica.
	 */
	@Test
	void testGetEquipos() throws EntityNotFoundException {
		List<EquipoEntity> equipoEntities = competenciaEquipoService.getEquiposParticipantes(competencia.getId());

		assertEquals(equipoList.size(), equipoEntities.size());

		for (int i = 0; i < equipoList.size(); i++) {
			assertTrue(equipoEntities.contains(equipoList.get(0)));
		}
	}

    /**
	 * Prueba para consultar la lista de equipos participantede de una competencia que no existe.
	 */
	@Test
	void testGetEquiposInvalidCompetencia(){
		assertThrows(EntityNotFoundException.class, ()->{
			competenciaEquipoService.getEquiposParticipantes(0L);
		});
	}

    /**
	 * Prueba para consultar un equipo de una competencia.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void getEquipoParticipante() throws EntityNotFoundException, IllegalOperationException {
		EquipoEntity equipoEntity = equipoList.get(0);
		EquipoEntity equipo = competenciaEquipoService.getEquipoParticipante(competencia.getId(), equipoEntity.getId());
		assertNotNull(equipo);

		assertEquals(equipoEntity.getId(), equipo.getId());
		assertEquals(equipoEntity.getNombre(), equipo.getNombre());
		assertEquals(equipoEntity.getPaisProcedencia(), equipo.getPaisProcedencia());
		assertEquals(equipoEntity.getBanderaPais(), equipo.getBanderaPais());
		assertEquals(equipoEntity.getLogo(), equipo.getLogo());
        assertEquals(equipoEntity.getTorneosParticipados(), equipo.getTorneosParticipados());
        assertEquals(equipoEntity.getCompetenciasParticipadas(), equipo.getCompetenciasParticipadas());
        assertEquals(equipoEntity.getCompetenciasGanadas(), equipo.getCompetenciasGanadas());
        assertEquals(equipoEntity.getIntegrantes(), equipo.getIntegrantes());
	}

    /**
	 * Prueba para consultar un equipo que no existe de una competencia.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidEquipo()  {
		assertThrows(EntityNotFoundException.class, ()->{
			competenciaEquipoService.getEquipoParticipante(competencia.getId(), 0L);
		});
	}

    /**
	 * Prueba para consultar un equipo de una competencia que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetEquipoInvalidTorneo() {
		assertThrows(EntityNotFoundException.class, ()->{
			EquipoEntity equipoEntity = equipoList.get(0);
			competenciaEquipoService.getEquipoParticipante(0L, equipoEntity.getId());
		});
	}

    /**
	 * Prueba para obtener un equipo no asociado a una competencia.
	 *
	 */
	@Test
	void testGetNotAssociatedEquipo() {
		assertThrows(IllegalOperationException.class, ()->{
			CompetenciaEntity newCompetencia = factory.manufacturePojo(CompetenciaEntity.class);
			entityManager.persist(newCompetencia);
			EquipoEntity equipo = factory.manufacturePojo(EquipoEntity.class);
			entityManager.persist(equipo);
			competenciaEquipoService.getEquipoParticipante(newCompetencia.getId(), equipo.getId());
		});
	}
}
