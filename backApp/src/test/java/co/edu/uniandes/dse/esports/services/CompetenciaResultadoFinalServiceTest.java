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
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({CompetenciaResultadoFinalService.class, CompetenciaService.class})
class CompetenciaResultadoFinalServiceTest {
    @Autowired
    private CompetenciaResultadoFinalService competenciaResultadoFinalService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CompetenciaEntity competencia = new CompetenciaEntity();
	private List<ResultadoFinalEntity> resultadoFinalList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    /**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ResultadoFinalEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from CompetenciaEntity").executeUpdate();
	}

    /**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		competencia = factory.manufacturePojo(CompetenciaEntity.class);
		entityManager.persist(competencia);

		for (int i = 0; i < 3; i++) 
        {
			ResultadoFinalEntity entity = factory.manufacturePojo(ResultadoFinalEntity.class);
			entityManager.persist(entity);
			entity.setCompetenciaRelacionada(competencia);
			resultadoFinalList.add(entity);
			competencia.getResultadosFinales().add(entity);	
		}
	}

    /**
	 * Prueba para consultar la lista de resultados finales de una competencia.
	 */
	@Test
	void getResultadosFinales() throws EntityNotFoundException {
		List<ResultadoFinalEntity> resultadoFinalEntities = competenciaResultadoFinalService.getResultadosFinales(competencia.getId());

		assertEquals(resultadoFinalList.size(), resultadoFinalEntities.size());

		for (int i = 0; i < resultadoFinalList.size(); i++) {
			assertTrue(resultadoFinalEntities.contains(resultadoFinalList.get(0)));
		}
	}

    /**
	 * Prueba para consultar la lista de resultados finales de una competencia que no existe.
	 */
	@Test
	void testGetResultadosFinalesInvalidCompetencia(){
		assertThrows(EntityNotFoundException.class, ()->{
			competenciaResultadoFinalService.getResultadosFinales(0L);
		});
	}

}
