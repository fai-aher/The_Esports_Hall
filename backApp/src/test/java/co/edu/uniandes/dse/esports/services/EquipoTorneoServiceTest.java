package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import co.edu.uniandes.dse.esports.entities.TorneoEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EquipoTorneoService.class)
class EquipoTorneoServiceTest {


            //En cada clase se inyecta su servicio y el Entity Manager.
            @Autowired
            private EquipoTorneoService equipoTorneoService;
        
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
        //add torneo to equipo
        void testAddTorneoParticipado() throws EntityNotFoundException, IllegalOperationException {
    
            EquipoEntity newEquipo = factory.manufacturePojo(EquipoEntity.class);
            entityManager.persist(newEquipo);
            
            TorneoEntity torneo = factory.manufacturePojo(TorneoEntity.class);
            entityManager.persist(torneo);
            
            equipoTorneoService.addTorneoParticipado(newEquipo.getId(), torneo.getId());
            
            TorneoEntity lastTorneo = equipoTorneoService.getTorneoParticipado(newEquipo.getId(), torneo.getId());

            assertEquals(torneo.getId(), lastTorneo.getId());
            assertEquals(torneo.getNombreTorneo(), lastTorneo.getNombreTorneo());
            assertEquals(torneo.getImagenRepresentativa(), lastTorneo.getImagenRepresentativa());
            assertEquals(torneo.getEnlacePaginaWeb(), lastTorneo.getEnlacePaginaWeb());
            assertEquals(torneo.getOrganizador(), lastTorneo.getOrganizador());
            assertEquals(torneo.getVideojuego(), lastTorneo.getVideojuego());
    
    
        }

        @Test
        //get 
    
        void testGetTorneosParticipados() throws EntityNotFoundException {
    
            EquipoEntity equipoPrueba = equiposList.get(0);
    
            List<TorneoEntity> torneosPrueba = equipoPrueba.getTorneosParticipados();
    
            List<TorneoEntity> torneoEntities = equipoTorneoService.getTorneosParticipados(equipoPrueba.getId());
    
            assertNotNull(torneoEntities);
    
            assertEquals(torneosPrueba.size(), torneoEntities.size());
        
            for (int i = 0; i < torneosPrueba.size(); i++) {
    
                    TorneoEntity prueba1 = torneosPrueba.get(i);
                    TorneoEntity prueba2 = torneoEntities.get(i);
    
                    assertEquals(prueba1.getId(), prueba2.getId());
                    assertEquals(prueba1.getNombreTorneo(), prueba2.getNombreTorneo());
                    assertEquals(prueba1.getImagenRepresentativa(), prueba2.getImagenRepresentativa());
                    assertEquals(prueba1.getEnlacePaginaWeb(), prueba2.getEnlacePaginaWeb());
                    assertEquals(prueba1.getOrganizador(), prueba2.getOrganizador());
                    assertEquals(prueba1.getVideojuego(), prueba2.getVideojuego());
    
            }

        }


        @Test
        void testAddInvalidTorneo () {

                assertThrows (EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(1);

                        equipoTorneoService.addTorneoParticipado(equipoPrueba.getId(), 0L);

                });


        }

        @Test
        void testAddTorneoToInvalidEquipo () {

                assertThrows (EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(1);
                        equipoPrueba.setId(0L);

                        TorneoEntity torneoPrueba = factory.manufacturePojo(TorneoEntity.class);
                        entityManager.persist(torneoPrueba);

                        equipoTorneoService.addTorneoParticipado(equipoPrueba.getId(), torneoPrueba.getId());

                });


        }

        @Test
        void testReplaceTorneosParticipados () throws EntityNotFoundException{
                        
                        TorneoEntity torneoPrueba1 = factory.manufacturePojo(TorneoEntity.class);
                        entityManager.persist(torneoPrueba1); 

                        TorneoEntity torneoPrueba2 = factory.manufacturePojo(TorneoEntity.class);
                        entityManager.persist(torneoPrueba2);
                        
                        List<TorneoEntity> torneos1 = new ArrayList<>();
                        torneos1.add(torneoPrueba1);

                        List<TorneoEntity> torneos2 = new ArrayList<>();
                        torneos2.add(torneoPrueba2);

                        EquipoEntity equipoPrueba = equiposList.get(0);
                        equipoPrueba.setTorneosParticipados(torneos1);

                        List<TorneoEntity> torneosRemplazados = equipoTorneoService.replaceTorneosParticipados(equipoPrueba.getId(), torneos2);

                        assertNotNull(torneosRemplazados);

                        for (int i = 0; i < torneosRemplazados.size(); i++) {

                                assertEquals(torneosRemplazados.get(i).getId(), torneos2.get(i).getId());  
                                assertEquals(torneosRemplazados.get(i).getEnlacePaginaWeb(), torneos2.get(i).getEnlacePaginaWeb());  
                                assertEquals(torneosRemplazados.get(i).getEquiposParticipantes(), torneos2.get(i).getEquiposParticipantes()); 
                                assertEquals(torneosRemplazados.get(i).getFechaFinalizacion(), torneos2.get(i).getFechaFinalizacion()); 
                                assertEquals(torneosRemplazados.get(i).getImagenRepresentativa(), torneos2.get(i).getImagenRepresentativa()); 
                                assertEquals(torneosRemplazados.get(i).getJugador(), torneos2.get(i).getJugador());
                                assertEquals(torneosRemplazados.get(i).getOrganizador(), torneos2.get(i).getOrganizador());
                                assertEquals(torneosRemplazados.get(i).getPaisRealizacion(), torneos2.get(i).getPaisRealizacion());
                                assertEquals(torneosRemplazados.get(i).getVideojuego(), torneos2.get(i).getVideojuego());
                        }
                     

                        
                        

        }

        @Test
        void testReplaceInvalidTorneos () {

                assertThrows (EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(1);

                        TorneoEntity torneoPrueba = factory.manufacturePojo(TorneoEntity.class);
                        entityManager.persist(torneoPrueba);
                        torneoPrueba.setId(0L);

                        List<TorneoEntity> listaTorneos = new ArrayList<>();
                        listaTorneos.add(torneoPrueba);

                        equipoTorneoService.replaceTorneosParticipados(equipoPrueba.getId(), listaTorneos);

                });


        }

        @Test
        void testDeleteTorneosParticipados() throws EntityNotFoundException{

                EquipoEntity equipoPrueba = equiposList.get(1);

                TorneoEntity torneoPrueba = factory.manufacturePojo(TorneoEntity.class);
                entityManager.persist(torneoPrueba);

                List<TorneoEntity> listaTorneos = new ArrayList<>();
                listaTorneos.add(torneoPrueba);

                equipoPrueba.setTorneosParticipados(listaTorneos);

                equipoTorneoService.removeTorneoParticipado(equipoPrueba.getId(), torneoPrueba.getId());

                assertThrows(IllegalOperationException.class, () -> {

                        equipoTorneoService.getTorneoParticipado(equipoPrueba.getId(), torneoPrueba.getId());

                });

                }

        @Test
        void testDeleteInvalidTorneosParticipados(){ 

                assertThrows(EntityNotFoundException.class, () -> {

                                EquipoEntity equipoPrueba = equiposList.get(1);

                                TorneoEntity torneoPrueba = factory.manufacturePojo(TorneoEntity.class);
                                entityManager.persist(torneoPrueba);
                
                                List<TorneoEntity> listaTorneos = new ArrayList<>();
                                listaTorneos.add(torneoPrueba);
                
                                equipoPrueba.setTorneosParticipados(listaTorneos);

                                equipoTorneoService.removeTorneoParticipado(equipoPrueba.getId(), 0L);

                });



        }

        @Test
        void testGetTorneosParticipadosInvalidEquipo() {
                Long equipoId = 0L;
                assertThrows(EntityNotFoundException.class, () -> equipoTorneoService.getTorneosParticipados(equipoId));
        }

        @Test
        void testGetTorneosParticipadosWithInvalidEquipo() {
            Long equipoId = 0L;
        
            assertThrows(EntityNotFoundException.class, () -> equipoTorneoService.getTorneosParticipados(equipoId));
        }

        @Test
        void testGetTorneoParticipadoWithInvalidTorneo() {
                EquipoEntity equipoPrueba = equiposList.get(0);
                Long torneoId = 0L;

                assertThrows(EntityNotFoundException.class, () -> equipoTorneoService.getTorneoParticipado(equipoPrueba.getId(), torneoId));
        }

        @Test
        void testGetTorneoParticipadoWithInvalidEquipo() {
                Long equipoId = 0L;
                TorneoEntity torneoPrueba = factory.manufacturePojo(TorneoEntity.class);
                entityManager.persist(torneoPrueba);

                assertThrows(EntityNotFoundException.class, () -> equipoTorneoService.getTorneoParticipado(equipoId, torneoPrueba.getId()));
        }

        @Test
        void testGetTorneoParticipadoWithEquipoNotParticipated() throws EntityNotFoundException, IllegalOperationException {
            Long equipoId = equiposList.get(0).getId();
            TorneoEntity torneoPrueba = factory.manufacturePojo(TorneoEntity.class);
            entityManager.persist(torneoPrueba);
            Long torneoId = torneoPrueba.getId();
    
            assertThrows(IllegalOperationException.class,
                    () -> equipoTorneoService.getTorneoParticipado(equipoId, torneoId));
        }

        @Test
        void testReplaceTorneosParticipadosWithInvalidEquipo() {
            Long equipoId = 0L;
            List<TorneoEntity> torneosNuevos = new ArrayList<>();
            TorneoEntity torneoPrueba = factory.manufacturePojo(TorneoEntity.class);
            entityManager.persist(torneoPrueba);
            TorneoEntity torneoPrueba2 = factory.manufacturePojo(TorneoEntity.class);
            entityManager.persist(torneoPrueba2);
            
            torneosNuevos.add(0, torneoPrueba);
            torneosNuevos.add(1, torneoPrueba2);


            assertThrows(EntityNotFoundException.class, () -> equipoTorneoService.replaceTorneosParticipados(equipoId, torneosNuevos));
        }
    
        @Test
        void testReplaceTorneosParticipadosWithInvalidTorneo() {
            EquipoEntity equipoPrueba = equiposList.get(0);
            List<TorneoEntity> torneosPrueba = new ArrayList<>();
            TorneoEntity torneoPrueba = factory.manufacturePojo(TorneoEntity.class);
            entityManager.persist(torneoPrueba);
            TorneoEntity torneoPrueba2 = factory.manufacturePojo(TorneoEntity.class);
            entityManager.persist(torneoPrueba2);
            
            torneosPrueba.add(0, torneoPrueba);
            torneosPrueba.add(1, torneoPrueba2);
    
            List<TorneoEntity> torneosNuevos = new ArrayList<>();
            TorneoEntity torneoInvalido = factory.manufacturePojo(TorneoEntity.class);
            torneoInvalido.setId(0L);
            torneosNuevos.add(torneoInvalido);
    
            equipoPrueba.setTorneosParticipados(torneosPrueba);
    
            assertThrows(EntityNotFoundException.class, () -> equipoTorneoService.replaceTorneosParticipados(equipoPrueba.getId(), torneosNuevos));
        }

        @Test
        void testRemoveTorneoParticipado() throws EntityNotFoundException {
                EquipoEntity equipo = equiposList.get(0);
                TorneoEntity torneo = factory.manufacturePojo(TorneoEntity.class);
                entityManager.persist(torneo);

                equipoTorneoService.addTorneoParticipado(equipo.getId(), torneo.getId());
                equipoTorneoService.removeTorneoParticipado(equipo.getId(), torneo.getId());
                assertFalse(equipo.getTorneosParticipados().contains(torneo));
        }

        @Test
        void testRemoveInvalidTorneoParticipado() throws EntityNotFoundException{
                EquipoEntity equipo = equiposList.get(0);
                TorneoEntity torneo = factory.manufacturePojo(TorneoEntity.class);
                entityManager.persist(torneo);

                equipoTorneoService.addTorneoParticipado(equipo.getId(), torneo.getId());
                assertThrows(EntityNotFoundException.class, () -> {
                        equipoTorneoService.removeTorneoParticipado(equipo.getId(), 0L);
                });
        }

        @Test
        void testRemoveInvalidEquipoTorneoParticipado() throws EntityNotFoundException{
                EquipoEntity equipo = equiposList.get(0);
                TorneoEntity torneo = factory.manufacturePojo(TorneoEntity.class);
                entityManager.persist(torneo);

                equipoTorneoService.addTorneoParticipado(equipo.getId(), torneo.getId());
                
                assertThrows(EntityNotFoundException.class, () -> {
                        equipoTorneoService.removeTorneoParticipado(0L, torneo.getId());
                });
        }

        @Test
        void testReplaceTorneosParticipadosWithDuplicateTorneo() throws EntityNotFoundException {
            EquipoEntity equipo = equiposList.get(0);
            List<TorneoEntity> nuevosTorneos = new ArrayList<>();
            TorneoEntity torneoExistente = factory.manufacturePojo(TorneoEntity.class);
            entityManager.persist(torneoExistente);
            
            nuevosTorneos.add(torneoExistente);
            equipoTorneoService.replaceTorneosParticipados(equipo.getId(), nuevosTorneos);
            assertTrue(equipo.getTorneosParticipados().contains(torneoExistente));
            nuevosTorneos.add(torneoExistente);
            equipoTorneoService.replaceTorneosParticipados(equipo.getId(), nuevosTorneos);
            assertTrue(equipo.getTorneosParticipados().contains(torneoExistente));
            assertEquals(1, equipo.getTorneosParticipados().size());
        }


}
