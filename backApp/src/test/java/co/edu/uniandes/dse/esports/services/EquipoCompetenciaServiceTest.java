package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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
@Import(EquipoCompetenciaService.class)
class EquipoCompetenciaServiceTest {

    //En cada clase se inyecta su servicio y el Entity Manager.
    @Autowired
    private EquipoCompetenciaService equipoCompetenciaService;

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
    //add competencia to equipo
    void testAddCompetenciaParticipada() throws EntityNotFoundException, IllegalOperationException {

        EquipoEntity newEquipo = factory.manufacturePojo(EquipoEntity.class);
        entityManager.persist(newEquipo);
        
        CompetenciaEntity competencia = factory.manufacturePojo(CompetenciaEntity.class);
        entityManager.persist(competencia);
        
        equipoCompetenciaService.addCompetenciaParticipada(newEquipo.getId(), competencia.getId());
        
        CompetenciaEntity lastCompetencia = equipoCompetenciaService.getCompetenciaParticipada(newEquipo.getId(), competencia.getId());
        assertEquals(competencia.getId(), lastCompetencia.getId());
        assertEquals(competencia.getEquiposParticipantes(), lastCompetencia.getEquiposParticipantes());
        assertEquals(competencia.getMvp(), lastCompetencia.getMvp());
        assertEquals(competencia.getDuracion(), lastCompetencia.getDuracion());


    }

    @Test
    //get 

    void testGetCompetenciasParticipadas() throws EntityNotFoundException {

        EquipoEntity equipoPrueba = equiposList.get(0);

        List<CompetenciaEntity> competenciasPrueba = equipoPrueba.getCompetenciasParticipadas();

        List<CompetenciaEntity> competenciaEntities = equipoCompetenciaService.getCompetenciasParticipadas(equipoPrueba.getId());

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
        void testAddInvalidCompetencia() {

                assertThrows(EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);

                        equipoCompetenciaService.addCompetenciaParticipada(equipoPrueba.getId(), 0L);

                });
        }

        @Test
        void testAddNullompetencia() {

                assertThrows(IllegalOperationException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);

                        equipoCompetenciaService.addCompetenciaParticipada(equipoPrueba.getId(), null);

                });
        }

        @Test
        void testGetCompetenciaParticipada() throws IllegalOperationException, EntityNotFoundException{

                EquipoEntity equipoPrueba = equiposList.get(0);

                CompetenciaEntity competenciaPrueba = competenciasList.get(0);

                equipoPrueba.setCompetenciasParticipadas(competenciasList);

                CompetenciaEntity competenciaEntity = equipoCompetenciaService.getCompetenciaParticipada(equipoPrueba.getId(), competenciaPrueba.getId());

                assertNotNull(competenciaEntity);

                CompetenciaEntity prueba1 = competenciaPrueba;
                CompetenciaEntity prueba2 = competenciaEntity;

                assertEquals(prueba1.getId(), prueba2.getId());
                assertEquals(prueba1.getEquiposParticipantes(), prueba2.getEquiposParticipantes());
                assertEquals(prueba1.getMvp(), prueba2.getMvp());
                assertEquals(prueba1.getDuracion(), prueba2.getDuracion());



        }

        @Test
        void testGetInvalidCompetenciaParticipada() {

                assertThrows (EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);
        
                        equipoPrueba.setCompetenciasParticipadas(competenciasList);
        
                        equipoCompetenciaService.getCompetenciaParticipada(equipoPrueba.getId(), 0L);
        


                });

        }

        @Test
        void testGetNotParticipatedCompetencia() {


                assertThrows (IllegalOperationException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(1);
                        CompetenciaEntity competencia0 = competenciasList.get(0);
                        CompetenciaEntity competencia1 = competenciasList.get(1);
                        CompetenciaEntity competencia2 = competenciasList.get(2);

                        List<CompetenciaEntity> listaCompetencias = new ArrayList<>();
                        listaCompetencias.add(competencia2);
                        listaCompetencias.add(competencia1);

                        equipoPrueba.setCompetenciasParticipadas(listaCompetencias);

                        equipoCompetenciaService.getCompetenciaParticipada(equipoPrueba.getId(), competencia0.getId());

                });

        }


        @Test
        void testReplaceNullCompetenciasParticipadas() {

                assertThrows (IllegalOperationException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);
        
                        equipoPrueba.setCompetenciasParticipadas(competenciasList);
        
                        equipoCompetenciaService.replaceCompetenciasParticipadas(equipoPrueba.getId(), null);
        


                });

        }

        @Test
        void testReplaceInvalidCompetenciasParticipadas() {

                assertThrows (EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);

                        List<CompetenciaEntity> competenciasVacias = new ArrayList<>();

                        CompetenciaEntity competencia0L = competenciasList.get(1);

                        competencia0L.setId(0L);

                        competenciasVacias.add(competencia0L);
        
                        equipoPrueba.setCompetenciasParticipadas(competenciasList);
        
                        equipoCompetenciaService.replaceCompetenciasParticipadas(equipoPrueba.getId(), competenciasVacias);
        


                });

        }

        @Test
        void testRemoveCompetenciaParticipada() throws EntityNotFoundException, IllegalOperationException{

                        EquipoEntity equipoPrueba = equiposList.get(0);

                        equipoPrueba.setCompetenciasParticipadas(competenciasList);

                        CompetenciaEntity unaCompetencia = competenciasList.get(1);

                        equipoCompetenciaService.removeCompetenciaParticipada(equipoPrueba.getId(), unaCompetencia.getId());

                        for (int i = 0; i < competenciasList.size(); i++) {

                                assertNotEquals(equipoPrueba.getCompetenciasParticipadas().get(i).getId(), unaCompetencia.getId());    

                        }
        

         }

         @Test
         void testRemoveInvalidCompetenciaParticipada(){


                assertThrows(EntityNotFoundException.class, () -> {

                        EquipoEntity equipoPrueba = equiposList.get(0);
 
                        equipoPrueba.setCompetenciasParticipadas(competenciasList);

                        equipoCompetenciaService.removeCompetenciaParticipada(equipoPrueba.getId(), 0L);


                });
 
 
          }

          @Test
          void testRemoveInvalidEquipoCompetenciaParticipada(){
 
 
                 assertThrows(EntityNotFoundException.class, () -> {
 
                         EquipoEntity equipoPrueba = equiposList.get(0);
  
                         equipoPrueba.setCompetenciasParticipadas(competenciasList);

                         CompetenciaEntity unaCompetencia = competenciasList.get(1);
 
                         equipoCompetenciaService.removeCompetenciaParticipada(0L, unaCompetencia.getId());
 
 
                 });
  
  
           }

       
           @Test
           // Test add competencia participada con id null
           void testAddCompetenciaParticipadaNull() {
               
               EquipoEntity equipoPrueba = equiposList.get(0);
       
               assertThrows(IllegalOperationException.class, () -> equipoCompetenciaService.addCompetenciaParticipada(equipoPrueba.getId(), null));
           }
       
           @Test
           // Test add competencia participada a equipo inexistente
           void testAddCompetenciaParticipadaInvalidEquipo() {
               
               Long equipoId = Long.MAX_VALUE;
       
               assertThrows(EntityNotFoundException.class, () -> equipoCompetenciaService.addCompetenciaParticipada(equipoId, competenciasList.get(0).getId()));
           }
       
           @Test
           // Test add competencia participada que ya existe
           void testAddCompetenciaParticipadaDuplicated() {
               
               EquipoEntity equipoPrueba = equiposList.get(0);
       
               CompetenciaEntity competenciaPrueba = equipoPrueba.getCompetenciasParticipadas().get(0);
       
               assertThrows(IllegalOperationException.class, () -> equipoCompetenciaService.addCompetenciaParticipada(equipoPrueba.getId(), competenciaPrueba.getId()));
           }

          
          @Test
          // Test get competencias participadas cuando el equipo no existe

          void testGetCompetenciasParticipadasInvalidEquipo() {

                assertThrows(EntityNotFoundException.class, () -> equipoCompetenciaService.getCompetenciasParticipadas(999L));
                
          }

        @Test
        void testReplaceCompetenciasParticipadas() throws EntityNotFoundException, IllegalOperationException {
                EquipoEntity equipoPrueba = equiposList.get(0);
                equipoPrueba.setCompetenciasParticipadas(competenciasList);

                List<CompetenciaEntity> nuevasCompetencias = new ArrayList<>();
                nuevasCompetencias.add(competenciasList.get(0));
                nuevasCompetencias.add(competenciasList.get(1));
                nuevasCompetencias.add(competenciasList.get(2));
                nuevasCompetencias.add(competenciasList.get(3));

                List<CompetenciaEntity> competenciasReemplazadas = equipoCompetenciaService.replaceCompetenciasParticipadas(equipoPrueba.getId(), nuevasCompetencias);

                assertNotNull(competenciasReemplazadas);
                assertEquals(competenciasReemplazadas.size(), nuevasCompetencias.size());
                assertTrue(equipoPrueba.getCompetenciasParticipadas().containsAll(nuevasCompetencias));
        }

        @Test
        void testReplaceCompetenciasParticipadasWithNullList() throws EntityNotFoundException, IllegalOperationException {
                EquipoEntity equipoPrueba = equiposList.get(0);

                assertThrows(IllegalOperationException.class,
                        () -> equipoCompetenciaService.replaceCompetenciasParticipadas(equipoPrueba.getId(), null));
        }

        @Test
        void testReplaceCompetenciasParticipadasWithInvalidEquipo() {
                Long equipoId = 0L;
                List<CompetenciaEntity> nuevasCompetencias = new ArrayList<>();
                nuevasCompetencias.add(competenciasList.get(0));

                assertThrows(EntityNotFoundException.class,
                        () -> equipoCompetenciaService.replaceCompetenciasParticipadas(equipoId, nuevasCompetencias));
        }

        @Test
        void testReplaceCompetenciasParticipadasWithInvalidCompetencia() {
                assertThrows(EntityNotFoundException.class, () -> {
                        EquipoEntity equipoPrueba = equiposList.get(0);
                        equipoPrueba.setId(4L);
                        List<CompetenciaEntity> nuevasCompetencias = new ArrayList<>();

                        nuevasCompetencias.add(factory.manufacturePojo(CompetenciaEntity.class));
                        nuevasCompetencias.add(factory.manufacturePojo(CompetenciaEntity.class));


                        equipoCompetenciaService.replaceCompetenciasParticipadas(equipoPrueba.getId(), nuevasCompetencias);
                });
        }

        @Test
        void testGetCompetenciaParticipadaWithInvalidEquipo() {
            Long equipoId = 0L;
            Long competenciaId = competenciasList.get(0).getId();
            
            assertThrows(EntityNotFoundException.class, () -> equipoCompetenciaService.getCompetenciaParticipada(equipoId, competenciaId));
        }

        @Test
        void testAddCompetenciaParticipadaAlreadyAssigned() throws EntityNotFoundException, IllegalOperationException {

                EquipoEntity equipoPrueba = equiposList.get(0);
                CompetenciaEntity competenciaPrueba = competenciasList.get(1);

                equipoPrueba.setCompetenciasParticipadas(Arrays.asList(competenciaPrueba));

                assertThrows(IllegalOperationException.class,
                () -> equipoCompetenciaService.addCompetenciaParticipada(equipoPrueba.getId(), competenciaPrueba.getId()));
        }

        @Test
        void testAddCompetenciaParticipadaNonExistentCompetencia() {
        assertThrows(EntityNotFoundException.class, () -> {
                EquipoEntity equipo = equiposList.get(0);
                equipoCompetenciaService.addCompetenciaParticipada(equipo.getId(), 0L);
        });
        }

        @Test
        void testAddCompetenciaParticipadaNonExistentEquipo() {
        assertThrows(EntityNotFoundException.class, () -> {
                CompetenciaEntity competencia = competenciasList.get(0);
                equipoCompetenciaService.addCompetenciaParticipada(0L, competencia.getId());
        });
        }

        @Test
        void testAddCompetenciaParticipadaAlreadyAdded() throws EntityNotFoundException, IllegalOperationException {

                //Se obtiene un equipo
                EquipoEntity equipo = equiposList.get(0);
                List<CompetenciaEntity> competencias = new ArrayList<>();
                equipo.setCompetenciasParticipadas(competencias);
                
                //Se obtiene una competencia
                CompetenciaEntity competencia = competenciasList.get(2);

                long equipoId = equipo.getId();
                long competenciaId = competencia.getId();
                
                //Se agrega la competencia participada al equipo por primera vez
                equipoCompetenciaService.addCompetenciaParticipada(equipoId, competenciaId);
                
                assertThrows(IllegalOperationException.class,
                () -> 
                //Se intenta agregar la misma competencia participada al equipo nuevamente
                equipoCompetenciaService.addCompetenciaParticipada(equipoId, competenciaId));
        }
        
}
