package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;

import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EquipoCompetenciasGanadasService.class)
class EquipoCompetenciasGanadasServiceTest {

        //En cada clase se inyecta su servicio y el Entity Manager.
        @Autowired
        private EquipoCompetenciasGanadasService equipoCompetenciasGanadasService;
    
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
        //get 
    
        void testGetCompetenciasParticipadas() throws EntityNotFoundException {
    
            EquipoEntity equipoPrueba = equiposList.get(0);
    
            List<CompetenciaEntity> competenciasPrueba = equipoPrueba.getCompetenciasGanadas();
    
            List<CompetenciaEntity> competenciaEntities = equipoCompetenciasGanadasService.getCompetenciasGanadas(equipoPrueba.getId());
    
            assertNotNull(competenciaEntities);
    
            assertEquals(competenciasPrueba.size(), competenciaEntities.size());
        
            for (int i = 0; i < competenciasPrueba.size(); i++) {
    
                    CompetenciaEntity prueba1 = competenciasPrueba.get(i);
                    CompetenciaEntity prueba2 = competenciaEntities.get(i);
    
    
                    assertEquals(prueba1.getId(), prueba2.getId());
                    assertEquals(prueba1.getEquiposParticipantes(), prueba2.getEquiposParticipantes());
                    assertEquals(prueba1.getMvp(), prueba2.getMvp());
                    assertEquals(prueba1.getDuracion(), prueba2.getDuracion());
    
            }
            
        }

        @Test
        //Test para lanzar una excepcion si el equipo no se encuentra
        void testGetCompetenciasInvalidEquipo() {

                assertThrows(EntityNotFoundException.class, () -> {

                        equipoCompetenciasGanadasService.getCompetenciasGanadas(0L);

                });
        }

        @Test
        void testGetCompetenciaGanada() throws EntityNotFoundException, IllegalOperationException {

                EquipoEntity equipoPrueba = equiposList.get(0);

                equipoPrueba.setCompetenciasGanadas(competenciasList);
    
                List<CompetenciaEntity> competenciasPrueba = equipoPrueba.getCompetenciasGanadas();

                CompetenciaEntity unaCompetencia = competenciasPrueba.get(0);
        
                CompetenciaEntity competenciaEntity = equipoCompetenciasGanadasService.getCompetenciaGanada(equipoPrueba.getId(), unaCompetencia.getId());
        
                assertNotNull(competenciaEntity);      

                CompetenciaEntity prueba1 = unaCompetencia;
                CompetenciaEntity prueba2 = competenciaEntity;

                assertEquals(prueba1.getId(), prueba2.getId());
                assertEquals(prueba1.getEquiposParticipantes(), prueba2.getEquiposParticipantes());
                assertEquals(prueba1.getMvp(), prueba2.getMvp());
                assertEquals(prueba1.getDuracion(), prueba2.getDuracion());

        }

        @Test
        void testGetInvalidCompetenciaGanada() {

                assertThrows(EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);

                        equipoPrueba.setCompetenciasGanadas(competenciasList);
            
                        List<CompetenciaEntity> competenciasPrueba = equipoPrueba.getCompetenciasGanadas();
        
                        competenciasPrueba.get(0);
                
                        CompetenciaEntity competenciaEntity = equipoCompetenciasGanadasService.getCompetenciaGanada(equipoPrueba.getId(), 0L);
                
                        assertNull(competenciaEntity);      


                });

        }

        @Test
        void testGetUnexistentEquipoCompetenciaGanada() {

                assertThrows (EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);
                        CompetenciaEntity competenciaPrueba = competenciasList.get(1);
                        
                        equipoPrueba.setCompetenciasGanadas(competenciasList);
                        equipoPrueba.setId(0L);

                        equipoCompetenciasGanadasService.getCompetenciaGanada(equipoPrueba.getId(), competenciaPrueba.getId());


                });
        }

        @Test
        void testGetNotRelatedCompetenciaGanada() {

                assertThrows (IllegalOperationException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);

                        List<CompetenciaEntity> someCompetencias = new ArrayList<>();

                        someCompetencias.add(competenciasList.get(0));
                        someCompetencias.add(competenciasList.get(1));

                        CompetenciaEntity competenciaPrueba = competenciasList.get(2);
                        
                        equipoPrueba.setCompetenciasGanadas(someCompetencias);

                        equipoCompetenciasGanadasService.getCompetenciaGanada(equipoPrueba.getId(), competenciaPrueba.getId()) ;


                });
        }


}