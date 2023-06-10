package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import co.edu.uniandes.dse.esports.entities.TorneoEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(CompetenciaTorneoService.class)
class CompetenciaTorneoServiceTest {

    @Autowired
    private CompetenciaTorneoService competenciaTorneoService;

    @Autowired
    private TestEntityManager entityManager;

    //Referencia a Podam
    private PodamFactory factory = new PodamFactoryImpl();


    private List<CompetenciaEntity> competenciasList = new ArrayList<>();
    private List<TorneoEntity> torneosList = new ArrayList<>();

    //Configuracion inicial de la prueba
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    // Definicion de los metodos para realizar las pruebas

        /**
         * 1. clearData()
         * Limpia las tablas que están implicadas en la prueba.
         */

        private void clearData() {
            entityManager.getEntityManager().createQuery("delete from TorneoEntity");
            entityManager.getEntityManager().createQuery("delete from CompetenciaEntity");
    }

    /**
     * insertData()
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {

        for (int i = 0; i < 3; i++) {
            TorneoEntity torneoEntity = factory.manufacturePojo(TorneoEntity.class);
            entityManager.persist(torneoEntity);
            torneosList.add(torneoEntity);
        }

        for (int i = 0; i < 3; i++) {
            CompetenciaEntity competenciaEntity = factory.manufacturePojo(CompetenciaEntity.class);
            competenciaEntity.setTorneo(torneosList.get(i));
            entityManager.persist(competenciaEntity);
            competenciasList.add(competenciaEntity);
        }
    }

@Test
void testAddTorneoCompetencia() throws EntityNotFoundException, IllegalOperationException {

        TorneoEntity newTorneo = factory.manufacturePojo(TorneoEntity.class);
        entityManager.persist(newTorneo);
        
        CompetenciaEntity newCompetencia = factory.manufacturePojo(CompetenciaEntity.class);
        entityManager.persist(newCompetencia);
        
        competenciaTorneoService.addTorneoCompetencia(newCompetencia.getId(), newTorneo.getId());
        TorneoEntity lastTorneo = competenciaTorneoService.getTorneoCompetencia(newCompetencia.getId());

        TorneoEntity prueba1 = newTorneo;
        TorneoEntity prueba2 = lastTorneo;

        assertEquals(prueba1.getId(), prueba2.getId());
        assertEquals(prueba1.getNombreTorneo(), prueba2.getNombreTorneo());
        assertEquals(prueba1.getPaisRealizacion(), prueba2.getPaisRealizacion());
        assertEquals(prueba1.getEnlacePaginaWeb(), prueba2.getEnlacePaginaWeb());
        assertEquals(prueba1.getOrganizador(), prueba2.getOrganizador());
        assertEquals(prueba1.getVideojuego(), prueba2.getVideojuego());
        assertEquals(prueba1.getEquiposParticipantes(), prueba2.getEquiposParticipantes());

    }

    @Test
    void testGetTorneoCompetencia() throws EntityNotFoundException {

        CompetenciaEntity competenciaEntity = factory.manufacturePojo(CompetenciaEntity.class);
        entityManager.persist(competenciaEntity);

        TorneoEntity torneoPrueba = torneosList.get(0);

        competenciaEntity.setTorneo(torneoPrueba);
        
        TorneoEntity torneoEntity = competenciaTorneoService.getTorneoCompetencia(competenciaEntity.getId());

        assertNotNull(torneoEntity);
    
        TorneoEntity prueba1 = torneoPrueba;
        TorneoEntity prueba2 = torneoEntity;

        assertEquals(prueba1.getId(), prueba2.getId());
        assertEquals(prueba1.getEnlacePaginaWeb(), prueba2.getEnlacePaginaWeb());
        assertEquals(prueba1.getFechaFinalizacion(), prueba2.getFechaFinalizacion());
        assertEquals(prueba1.getEquiposParticipantes(), prueba2.getEquiposParticipantes());
        assertEquals(prueba1.getImagenRepresentativa(), prueba2.getImagenRepresentativa());
        assertEquals(prueba1.getNombreTorneo(), prueba2.getNombreTorneo());
        assertEquals(prueba1.getPaisRealizacion(), prueba2.getPaisRealizacion());
    }

    @Test
    void testAddInvalidTorneo() {

            assertThrows(EntityNotFoundException.class , () -> {
                    CompetenciaEntity newEntity = factory.manufacturePojo(CompetenciaEntity.class);
                    entityManager.persist(newEntity);

                    competenciaTorneoService.addTorneoCompetencia(newEntity.getId(), 0L);
                });
    }

}