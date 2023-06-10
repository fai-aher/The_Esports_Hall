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

import co.edu.uniandes.dse.esports.entities.JugadorEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EquipoJugadorService.class)
class EquipoJugadorServiceTest {

        //En cada clase se inyecta su servicio y el Entity Manager.
        @Autowired
        private EquipoJugadorService equipoJugadorService;
    
        @Autowired
        private TestEntityManager entityManager;
    
        //Referencia a Podam
        private PodamFactory factory = new PodamFactoryImpl();
    
        //Estructuras de datos para las pruebas
    
        private List<JugadorEntity> jugadoresList = new ArrayList<>();
        private List<EquipoEntity> equiposList = new ArrayList<>();
        private List<CompetenciaEntity> competenciasList = new ArrayList<>();
    
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
                entityManager.getEntityManager().createQuery("delete from EquipoEntity");
                entityManager.getEntityManager().createQuery("delete from JugadorEntity");
                entityManager.getEntityManager().createQuery("delete from CompetenciaEntity");
        }
    
        /**
         * insertData()
         * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
         */
        private void insertData() {
                for (int i = 0; i < 3; i++) {
                        JugadorEntity jugadorEntity = factory.manufacturePojo(JugadorEntity.class);
                        entityManager.persist(jugadorEntity);
                        jugadoresList.add(jugadorEntity);
                }
    
                for (int i = 0; i < 3; i++) {
                        CompetenciaEntity competenciaEntity = factory.manufacturePojo(CompetenciaEntity.class);
                        entityManager.persist(competenciaEntity);
                        competenciasList.add(competenciaEntity);
                }
    
                for (int i = 0; i < 3; i++) {
                        EquipoEntity equipoEntity = factory.manufacturePojo(EquipoEntity.class);
                        equipoEntity.setIntegrantes(jugadoresList);
                        equipoEntity.setCompetenciasParticipadas(competenciasList);
                        entityManager.persist(equipoEntity);
                        equiposList.add(equipoEntity);
                }
    
    
                CompetenciaEntity competenciaEntity = factory.manufacturePojo(CompetenciaEntity.class);
                entityManager.persist(competenciaEntity);
                competenciaEntity.getEquiposParticipantes().add(equiposList.get(0));
                equiposList.get(0).getCompetenciasParticipadas().add(competenciaEntity);
        }


    @Test
    // Test get jugadores

    void testGetIntegrantes() throws EntityNotFoundException {
    
        EquipoEntity equipoPrueba = equiposList.get(0);

        List<JugadorEntity> integrantesPrueba = equipoPrueba.getIntegrantes();

        List<JugadorEntity> jugadorEntities = equipoJugadorService.getIntegrantes(equipoPrueba.getId());

        assertNotNull(jugadorEntities);

        assertEquals(integrantesPrueba.size(), jugadorEntities.size());
    
        for (int i = 0; i < integrantesPrueba.size(); i++) {

                JugadorEntity prueba1 = integrantesPrueba.get(i);
                JugadorEntity prueba2 = jugadorEntities.get(i);


                assertEquals(prueba1.getId(), prueba2.getId());
                assertEquals(prueba1.getNombre(), prueba2.getNombre());
                assertEquals(prueba1.getNickname(), prueba2.getNickname());
                assertEquals(prueba1.getNacionalidad(), prueba2.getNacionalidad());
                assertEquals(prueba1.getFotografia(), prueba2.getFotografia());
                assertEquals(prueba1.getFechaNacimiento(), prueba2.getFechaNacimiento());

        }

    }

    @Test
    // Test get jugador integrante

    void testGetIntegrante() throws EntityNotFoundException, IllegalOperationException {
    
        EquipoEntity equipoPrueba = equiposList.get(0);

        List<JugadorEntity> integrantesPrueba = equipoPrueba.getIntegrantes();

        JugadorEntity integrantePrueba = integrantesPrueba.get(0);

        JugadorEntity jugadorEntity = equipoJugadorService.getIntegrante(equipoPrueba.getId(), integrantePrueba.getId());

        assertNotNull(jugadorEntity);

        JugadorEntity prueba1 = integrantePrueba;
        JugadorEntity prueba2 = jugadorEntity;


        assertEquals(prueba1.getId(), prueba2.getId());
        assertEquals(prueba1.getNombre(), prueba2.getNombre());
        assertEquals(prueba1.getNickname(), prueba2.getNickname());
        assertEquals(prueba1.getNacionalidad(), prueba2.getNacionalidad());
        assertEquals(prueba1.getFotografia(), prueba2.getFotografia());
        assertEquals(prueba1.getFechaNacimiento(), prueba2.getFechaNacimiento());

        }

        @Test
        void testGetIntegranteInvalidJugador() throws EntityNotFoundException, IllegalOperationException {
            EquipoEntity equipo = equiposList.get(0);
            JugadorEntity jugadorInvalido = factory.manufacturePojo(JugadorEntity.class);
            jugadorInvalido.setId(0L);
    
            assertThrows(EntityNotFoundException.class,
                    () -> equipoJugadorService.getIntegrante(equipo.getId(), jugadorInvalido.getId()));
        }
    
        @Test
        void testGetIntegranteJugadorNoAsociado() throws EntityNotFoundException, IllegalOperationException {
            EquipoEntity equipo = equiposList.get(0);
            JugadorEntity jugadorNoAsociado = factory.manufacturePojo(JugadorEntity.class);
            entityManager.persist(jugadorNoAsociado);
    
            assertThrows(IllegalOperationException.class,
                    () -> equipoJugadorService.getIntegrante(equipo.getId(), jugadorNoAsociado.getId()));
        }

        @Test
        // Test get integrantes de un equipo no existente
        void testGetIntegrantesInvalidEquipo() {
            Long equipoId = 0L;
            assertThrows(EntityNotFoundException.class, () -> equipoJugadorService.getIntegrantes(equipoId));
        }
    
        @Test
        // Test get integrante de un equipo no existente
        void testGetIntegranteInvalidEquipo() {
            Long equipoId = 0L;
            Long jugadorId = jugadoresList.get(0).getId();
            assertThrows(EntityNotFoundException.class, () -> equipoJugadorService.getIntegrante(equipoId, jugadorId));
        }
    
        @Test
        // Test get integrante no vinculado al equipo
        void testGetIntegranteInvalidVinculacion() throws EntityNotFoundException {
            EquipoEntity equipoPrueba = equiposList.get(0);
            JugadorEntity jugadorPrueba = factory.manufacturePojo(JugadorEntity.class);
            entityManager.persist(jugadorPrueba);
            assertThrows(IllegalOperationException.class, () -> equipoJugadorService.getIntegrante(equipoPrueba.getId(), jugadorPrueba.getId()));
        }    

}

