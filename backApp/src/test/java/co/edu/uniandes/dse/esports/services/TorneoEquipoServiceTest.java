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
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({TorneoEquipoService.class, TorneoService.class})
class TorneoEquipoServiceTest {
    @Autowired
    private TorneoEquipoService torneoEquipoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private TorneoEntity torneo = new TorneoEntity();
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
		entityManager.getEntityManager().createQuery("delete from TorneoEntity").executeUpdate();
	}

    /**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		torneo = factory.manufacturePojo(TorneoEntity.class);
		entityManager.persist(torneo);

		for (int i = 0; i < 3; i++) {
			EquipoEntity entity = factory.manufacturePojo(EquipoEntity.class);
			entityManager.persist(entity);
			entity.getTorneosParticipados().add(torneo);
			equipoList.add(entity);
			torneo.getEquiposParticipantes().add(entity);	
		}
	}

    /**
	 * Prueba para consultar la lista de equipos de un torneo.
	 */
	@Test
	void testGetEquipos() throws EntityNotFoundException {
		List<EquipoEntity> equipoEntities = torneoEquipoService.getEquipos(torneo.getId());

		assertEquals(equipoList.size(), equipoEntities.size());

		for (int i = 0; i < equipoList.size(); i++) {
			assertTrue(equipoEntities.contains(equipoList.get(0)));
		}
	}

    /**
	 * Prueba para consultar la lista de equipos de un torneo que no existe.
	 */
	@Test
	void testGetEquiposInvalidTorneo(){
		assertThrows(EntityNotFoundException.class, ()->{
			torneoEquipoService.getEquipos(0L);
		});
	}

    /**
	 * Prueba para consultar un equipo de un torneo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetEquipo() throws EntityNotFoundException, IllegalOperationException {
		EquipoEntity equipoEntity = equipoList.get(0);
		EquipoEntity equipo = torneoEquipoService.getEquipo(torneo.getId(), equipoEntity.getId());
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
	 * Prueba para consultar un equipo que no existe de un torneo.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidEquipo()  {
		assertThrows(EntityNotFoundException.class, ()->{
			torneoEquipoService.getEquipo(torneo.getId(), 0L);
		});
	}

    /**
	 * Prueba para consultar un equipo de un torneo que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetEquipoInvalidTorneo() {
		assertThrows(EntityNotFoundException.class, ()->{
			EquipoEntity equipoEntity = equipoList.get(0);
			torneoEquipoService.getEquipo(0L, equipoEntity.getId());
		});
	}

    /**
	 * Prueba para obtener un equipo no asociado a un torneo.
	 *
	 */
	@Test
	void testGetNotAssociatedEquipo() {
		assertThrows(IllegalOperationException.class, ()->{
			TorneoEntity newTorneo = factory.manufacturePojo(TorneoEntity.class);
			entityManager.persist(newTorneo);
			EquipoEntity equipo = factory.manufacturePojo(EquipoEntity.class);
			entityManager.persist(equipo);
			torneoEquipoService.getEquipo(newTorneo.getId(), equipo.getId());
		});
	}
}
