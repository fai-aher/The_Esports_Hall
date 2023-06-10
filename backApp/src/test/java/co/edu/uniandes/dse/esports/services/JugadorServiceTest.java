package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.entities.LogrosEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(JugadorService.class)
class JugadorServiceTest {
    

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private TestEntityManager entityManager;


    private PodamFactory factory = new PodamFactoryImpl();


    private List<JugadorEntity> jugadoresList = new ArrayList<>();
    private List<CompetenciaEntity> competenciasList = new ArrayList<>();
    private List<LogrosEntity> logrosList = new ArrayList<>();
    private List<EquipoEntity> equiposList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

        private void clearData() {
            entityManager.getEntityManager().createQuery("delete from JugadorEntity");
            entityManager.getEntityManager().createQuery("delete from CompetenciaEntity");
            entityManager.getEntityManager().createQuery("delete from LogrosEntity");
            entityManager.getEntityManager().createQuery("delete from EquipoEntity");
    }


    private void insertData() {
        for (int i = 0; i < 3; i++) {
            CompetenciaEntity competenciaEntity = factory.manufacturePojo(CompetenciaEntity.class);
            entityManager.persist(competenciaEntity);
            competenciasList.add(competenciaEntity);
        }

        for (int i = 0; i < 3; i++) {
            LogrosEntity logrosEntity = factory.manufacturePojo(LogrosEntity.class);
            entityManager.persist(logrosEntity);
            logrosList.add(logrosEntity);
        }

        for (int i = 0; i < 3; i++) {
            EquipoEntity equipoEntity = factory.manufacturePojo(EquipoEntity.class);
            entityManager.persist(equipoEntity);
            equiposList.add(equipoEntity);
        }

        for (int i = 0; i < 3; i++) {
            JugadorEntity jugadorEntity = factory.manufacturePojo(JugadorEntity.class);
            jugadorEntity.setCompetenciasParticipadas(competenciasList);
            jugadorEntity.setLogrosObtenidos(logrosList);
            jugadorEntity.setEquipo(equiposList.get(0));
            entityManager.persist(jugadorEntity);
            jugadoresList.add(jugadorEntity);
        }

    }

    @Test
    void testCreateJugador() throws EntityNotFoundException, IllegalOperationException {

        JugadorEntity newEntity = factory.manufacturePojo(JugadorEntity.class);

        newEntity.setId(10L);
        newEntity.setCompetenciasParticipadas(competenciasList);
        newEntity.setLogrosObtenidos(logrosList);
        newEntity.setEquipo(equiposList.get(0));
        String sDate1="31/12/1998";  
        Date date1;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            newEntity.setFechaNacimiento(date1);
        } catch (ParseException e) {
        
            e.printStackTrace();
        };

        JugadorEntity result = jugadorService.createJugador(newEntity);

        assertNotNull(result);

        JugadorEntity entity = entityManager.find(JugadorEntity.class, result.getId());

        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getEquipo(), entity.getEquipo());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getNickname(), entity.getNickname());
        assertEquals(newEntity.getNacionalidad(), entity.getNacionalidad());
        assertEquals(newEntity.getFotografia(), entity.getFotografia());
        assertEquals(newEntity.getFechaNacimiento(), entity.getFechaNacimiento());
    }

    @Test
	void testCreateJugadorWithStoredId() {
		assertThrows(IllegalOperationException.class, () -> {
			JugadorEntity newEntity = factory.manufacturePojo(JugadorEntity.class);
			newEntity.setCompetenciasParticipadas(competenciasList);
            newEntity.setLogrosObtenidos(logrosList);
            newEntity.setEquipo(equiposList.get(0));
			newEntity.setId(jugadoresList.get(0).getId());
			jugadorService.createJugador(newEntity);
		});
	}

    @Test
    void testCreateJugadorEquipoNull() {

        assertThrows(IllegalOperationException.class, () -> {
            JugadorEntity newEntity = factory.manufacturePojo(JugadorEntity.class);

            newEntity.setCompetenciasParticipadas(competenciasList);
            newEntity.setLogrosObtenidos(logrosList);
            newEntity.setEquipo(null);

            jugadorService.createJugador(newEntity);
        });

    }

    @Test
	void testCreateJugadorInvalidFechaNacimiento() {

		assertThrows(IllegalOperationException.class, () -> {
			JugadorEntity newEntity = factory.manufacturePojo(JugadorEntity.class);
			Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2090");  
			newEntity.setFechaNacimiento(fecha);
			jugadorService.createJugador(newEntity);
		});
	}

    @Test
    void testGetAllJugadores() {

        List<JugadorEntity> listaPrueba = jugadorService.getAllJugadores();
        assertEquals(jugadoresList.size(), listaPrueba.size());

        for (JugadorEntity entity : listaPrueba) {
            boolean found = false;
            for (JugadorEntity storedEntity : jugadoresList) {
                if (entity.getId() == (storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetJugador() throws EntityNotFoundException {

        JugadorEntity entity = jugadoresList.get(0);
        JugadorEntity resultEntity  = jugadorService.getJugador(entity.getId());

        assertNotNull(resultEntity);

        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getCompetenciasParticipadas(), resultEntity.getCompetenciasParticipadas());
        assertEquals(entity.getLogrosObtenidos(), resultEntity.getLogrosObtenidos());
        assertEquals(entity.getEquipo(), resultEntity.getEquipo());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getNickname(), resultEntity.getNickname());
        assertEquals(entity.getNacionalidad(), resultEntity.getNacionalidad());
        assertEquals(entity.getFotografia(), resultEntity.getFotografia());
        assertEquals(entity.getFechaNacimiento(), resultEntity.getFechaNacimiento());
        assertEquals(entity.getTorneosParticipados(), resultEntity.getTorneosParticipados());

    }

    @Test
    void testGetInvalidJugador() {
        assertThrows(EntityNotFoundException.class, () -> {
            jugadorService.getJugador(0L);
        });

    }


    @Test
    void testUpdateJugador() throws EntityNotFoundException, IllegalOperationException {
        JugadorEntity entity = jugadoresList.get(0);
        entity.setCompetenciasParticipadas(competenciasList);
        entityManager.persist(entity);
        JugadorEntity pojoEntity = factory.manufacturePojo(JugadorEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setCompetenciasParticipadas(competenciasList);
        pojoEntity.setLogrosObtenidos(logrosList);
        pojoEntity.setEquipo(equiposList.get(0));
        jugadorService.updateJugador(entity.getId(), pojoEntity);

        JugadorEntity resp = entityManager.find(JugadorEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getCompetenciasParticipadas(), resp.getCompetenciasParticipadas());
        assertEquals(pojoEntity.getLogrosObtenidos(), resp.getLogrosObtenidos());
        assertEquals(pojoEntity.getEquipo(), resp.getEquipo());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getNickname(), resp.getNickname());
        assertEquals(pojoEntity.getNacionalidad(), resp.getNacionalidad());
        assertEquals(pojoEntity.getFotografia(), resp.getFotografia());
        assertEquals(pojoEntity.getFechaNacimiento(), resp.getFechaNacimiento());
        assertEquals(pojoEntity.getTorneosParticipados(), resp.getTorneosParticipados());

    }

    @Test
    void testUpdateInvalidJugador() {

        assertThrows(EntityNotFoundException.class, () -> {

            JugadorEntity pojoEntity = factory.manufacturePojo(JugadorEntity.class);
            pojoEntity.setId(0L);
            jugadorService.updateJugador(0L, pojoEntity);

        });

    }

    @Test
    void testUpdateJugadorWithNoCompetencias() {

        assertThrows(IllegalOperationException.class, () -> {

            JugadorEntity entity = jugadoresList.get(0);
            JugadorEntity pojoEntity = factory.manufacturePojo(JugadorEntity.class);

            List<CompetenciaEntity> competenciasVacias = new ArrayList<>();

            pojoEntity.setCompetenciasParticipadas(competenciasVacias);
            pojoEntity.setId(entity.getId());
            jugadorService.updateJugador(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testUpdateJugadorWithNullCompetencias() {

        assertThrows(IllegalOperationException.class, () -> {

            JugadorEntity entity = jugadoresList.get(0);
            JugadorEntity pojoEntity = factory.manufacturePojo(JugadorEntity.class);

            pojoEntity.setCompetenciasParticipadas(null);
            pojoEntity.setId(entity.getId());

            jugadorService.updateJugador(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testUpdateJugadorWithNoLogros() {

        assertThrows(IllegalOperationException.class, () -> {

            JugadorEntity entity = jugadoresList.get(0);
            JugadorEntity pojoEntity = factory.manufacturePojo(JugadorEntity.class);

            List<LogrosEntity> logrosVacios = new ArrayList<>();

            pojoEntity.setLogrosObtenidos(logrosVacios);
            pojoEntity.setId(entity.getId());
            jugadorService.updateJugador(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testUpdateJugadorWithNullLogros() {

        assertThrows(IllegalOperationException.class, () -> {

            JugadorEntity entity = jugadoresList.get(0);
            JugadorEntity pojoEntity = factory.manufacturePojo(JugadorEntity.class);

            pojoEntity.setLogrosObtenidos(null);
            pojoEntity.setId(entity.getId());

            jugadorService.updateJugador(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testUpdateJugadorWithNullEquipo() {

        assertThrows(IllegalOperationException.class, () -> {

            JugadorEntity entity = jugadoresList.get(0);
            JugadorEntity pojoEntity = factory.manufacturePojo(JugadorEntity.class);

            pojoEntity.setEquipo(null);
            pojoEntity.setId(entity.getId());

            jugadorService.updateJugador(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testDeleteJugador() throws EntityNotFoundException, IllegalOperationException {

        JugadorEntity entity = jugadoresList.get(1);

        jugadorService.deleteJugador(entity.getId());
        
        JugadorEntity deleted = entityManager.find(JugadorEntity.class, entity.getId());

        assertNull(deleted);

    }

    @Test
    void testDeleteInvalidJugador() {

        assertThrows(EntityNotFoundException.class, () -> {

            jugadorService.deleteJugador(0L);
        });

    }
}
