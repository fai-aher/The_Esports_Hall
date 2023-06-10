package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.repositories.CompetenciaRepository;
import co.edu.uniandes.dse.esports.repositories.ResultadoFinalRepository;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ResultadoFinalCompetenciaService.class)
class ResultadoFinalCompetenciaServiceTest {

        @Autowired
        private ResultadoFinalCompetenciaService resultadoFinalCompetenciaService;

        @Autowired
        private ResultadoFinalRepository resultadoFinalRepository;
    
        @Autowired
        private CompetenciaRepository competenciaRepository;
    
        @Autowired
        private TestEntityManager entityManager;
    
        //Referencia a Podam
        private PodamFactory factory = new PodamFactoryImpl();
    
        private List<JugadorEntity> jugadoresList = new ArrayList<>();
        private List<EquipoEntity> equiposList = new ArrayList<>();
        private List<CompetenciaEntity> competenciasList = new ArrayList<>();
        private List<ResultadoFinalEntity> resultadosFinalesList = new ArrayList<>();
    
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
                entityManager.getEntityManager().createQuery("delete from ResultadoFinalEntity");
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
    
                for (int i = 0; i < 3; i++) {
                    ResultadoFinalEntity resultadoEntity = factory.manufacturePojo(ResultadoFinalEntity.class);
                    resultadoEntity.setCompetenciaRelacionada(competenciasList.get(i));
                    resultadoEntity.setEquipoInvolucrado(equiposList.get(i));
                    entityManager.persist(resultadoEntity);
                    resultadosFinalesList.add(resultadoEntity);
            }
    
    
                CompetenciaEntity competenciaEntity = factory.manufacturePojo(CompetenciaEntity.class);
                entityManager.persist(competenciaEntity);
                competenciaEntity.getEquiposParticipantes().add(equiposList.get(0));
                equiposList.get(0).getCompetenciasParticipadas().add(competenciaEntity);
        }

    @Test
    void testAddCompetenciaAsociada() throws EntityNotFoundException, IllegalOperationException {
    
            CompetenciaEntity newCompetencia = factory.manufacturePojo(CompetenciaEntity.class);
            entityManager.persist(newCompetencia);
            
            ResultadoFinalEntity newResultado = factory.manufacturePojo(ResultadoFinalEntity.class);
            entityManager.persist(newResultado);
            
            resultadoFinalCompetenciaService.addCompetenciaAsociada(newResultado.getId(), newCompetencia.getId());
            
            CompetenciaEntity lastCompetencia = resultadoFinalCompetenciaService.getCompetenciaAsociada(newResultado.getId());

            CompetenciaEntity prueba1 = newCompetencia;
            CompetenciaEntity prueba2 = lastCompetencia;

            assertEquals(prueba1.getId(), prueba2.getId());
            assertEquals(prueba1.getEquiposParticipantes(), prueba2.getEquiposParticipantes());
            assertEquals(prueba1.getMvp(), prueba2.getMvp());
            assertEquals(prueba1.getDuracion(), prueba2.getDuracion());
            assertEquals(prueba1.getTorneo(), prueba2.getTorneo());
            assertEquals(prueba1.getEquipoGanador(), prueba2.getEquipoGanador());
    
        }

    @Test
    void testAddCompetenciaAsociadaCompetenciaNoExistente() throws EntityNotFoundException, IllegalOperationException {
        ResultadoFinalEntity resultadoFinal= resultadosFinalesList.get(0);
        CompetenciaEntity competencia = factory.manufacturePojo(CompetenciaEntity.class);
        competencia.setId(0L);

        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalCompetenciaService.addCompetenciaAsociada(resultadoFinal.getId(), competencia.getId());
        });
    }

    @Test
    void testAddCompetenciaAsociadaResultadoFinalNoExistente() throws EntityNotFoundException, IllegalOperationException {
        CompetenciaEntity competencia = competenciasList.get(0);
        ResultadoFinalEntity resultadoFinal = factory.manufacturePojo(ResultadoFinalEntity.class);
        resultadoFinal.setId(0L);
    
        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalCompetenciaService.addCompetenciaAsociada(resultadoFinal.getId(), competencia.getId());
        });
    }


        @Test
        void testGetCompetenciaAsociada() throws EntityNotFoundException {

            ResultadoFinalEntity resultadoFinalEntity = factory.manufacturePojo(ResultadoFinalEntity.class);
            entityManager.persist(resultadoFinalEntity);

            CompetenciaEntity competenciaPrueba = competenciasList.get(0);
    
            resultadoFinalEntity.setCompetenciaRelacionada(competenciaPrueba);
            
            CompetenciaEntity competenciaEntity = resultadoFinalCompetenciaService.getCompetenciaAsociada(resultadoFinalEntity.getId());
    
            assertNotNull(competenciaEntity);
        
            CompetenciaEntity prueba1 = competenciaPrueba;
            CompetenciaEntity prueba2 = competenciaEntity;

            assertEquals(prueba1.getId(), prueba2.getId());
            assertEquals(prueba1.getEquiposParticipantes(), prueba2.getEquiposParticipantes());
            assertEquals(prueba1.getMvp(), prueba2.getMvp());
            assertEquals(prueba1.getDuracion(), prueba2.getDuracion());
            assertEquals(prueba1.getTorneo(), prueba2.getTorneo());
            assertEquals(prueba1.getEquipoGanador(), prueba2.getEquipoGanador());
        }

        @Test
        void testAddInvalidCompetencia() {

                assertThrows(EntityNotFoundException.class , () -> {
                        ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);
                        entityManager.persist(newEntity);
  
                        resultadoFinalCompetenciaService.addCompetenciaAsociada(newEntity.getId(), 0L);
            
                    });


        }

        @Test
        void testAddNullCompetencia() {

                assertThrows(IllegalOperationException.class , () -> {
                        ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);
                        entityManager.persist(newEntity);
  
                        resultadoFinalCompetenciaService.addCompetenciaAsociada(newEntity.getId(), null);
            
                    });


        }


        @Test
        void testReplaceCompetenciaAsociada() throws EntityNotFoundException, IllegalOperationException {
            ResultadoFinalEntity resultadoFinal= resultadosFinalesList.get(0);
            CompetenciaEntity competencia = competenciasList.get(1);
    
            CompetenciaEntity replaced = resultadoFinalCompetenciaService.replaceCompetenciaAsociada(resultadoFinal.getId(), competencia);
    
            assertEquals(replaced.getId(), competencia.getId());
            assertEquals(replaced.getEquiposParticipantes(), competencia.getEquiposParticipantes());
            assertEquals(replaced.getMvp(), competencia.getMvp());
            assertEquals(replaced.getDuracion(), competencia.getDuracion());
            assertEquals(replaced.getTorneo(), competencia.getTorneo());
            assertEquals(replaced.getEquipoGanador(), competencia.getEquipoGanador());
        }


        @Test
        void testRemoveCompetenciaAsociada() throws EntityNotFoundException {
            ResultadoFinalEntity resultadoFinal = resultadosFinalesList.get(0);
            resultadoFinal.setCompetenciaRelacionada(competenciasList.get(0));
            Long competenciaId = resultadoFinal.getCompetenciaRelacionada().getId();
        
            resultadoFinalCompetenciaService.removeCompetenciaAsociada(resultadoFinal.getId());
        
            ResultadoFinalEntity resultadoFinalActualizado = resultadoFinalRepository.findById(resultadoFinal.getId()).orElse(null);
            CompetenciaEntity competenciaActualizada = competenciaRepository.findById(competenciaId).orElse(null);
        
            assertNull(resultadoFinalActualizado.getCompetenciaRelacionada());
            assertFalse(competenciaActualizada.getResultadosFinales().contains(resultadoFinal));
        }


        @Test
        void testGetCompetenciaAsociadaEntityNotFound() {
            assertThrows(EntityNotFoundException.class, () -> {
                resultadoFinalCompetenciaService.getCompetenciaAsociada(0L);
            });
        }
        
        @Test
        void testGetCompetenciaAsociada2() throws EntityNotFoundException, IllegalOperationException {
            ResultadoFinalEntity resultadoFinal = resultadosFinalesList.get(0);
            CompetenciaEntity competencia = competenciasList.get(0);
        
            resultadoFinalCompetenciaService.addCompetenciaAsociada(resultadoFinal.getId(), competencia.getId());
        
            assertEquals(competencia, resultadoFinalCompetenciaService.getCompetenciaAsociada(resultadoFinal.getId()));
        }

        @Test
        void testReplaceCompetenciaAsociadaResultadoNoExistente() {
            // Intentar reemplazar la competencia asociada a un resultado que no existe
            assertThrows(EntityNotFoundException.class, () -> {
                resultadoFinalCompetenciaService.replaceCompetenciaAsociada(0L, competenciasList.get(0));
            });
        }

        @Test
        void testReplaceCompetenciaAsociadaCompetenciaNoExistente() {
            // Obtener un resultado final y una competencia inexistente
            ResultadoFinalEntity resultadoFinal = resultadosFinalesList.get(0);
            CompetenciaEntity competencia = factory.manufacturePojo(CompetenciaEntity.class);
            competencia.setId(0L);
        
            // Intentar reemplazar la competencia asociada al resultado con la competencia inexistente
            assertThrows(EntityNotFoundException.class, () -> {
                resultadoFinalCompetenciaService.replaceCompetenciaAsociada(resultadoFinal.getId(), competencia);
            });
        }

        @Test
        void testReplaceCompetenciaAsociadaMismaCompetencia() throws EntityNotFoundException, IllegalOperationException {
            // Obtener un resultado final y una competencia existente
            ResultadoFinalEntity resultadoFinal = resultadosFinalesList.get(0);
            CompetenciaEntity competencia = competenciasList.get(0);
        
            // Reemplazar la competencia asociada al resultado con la misma competencia
            CompetenciaEntity replaced = resultadoFinalCompetenciaService.replaceCompetenciaAsociada(resultadoFinal.getId(), competencia);
        
            // Verificar que la competencia asociada al resultado sigue siendo la misma
            assertEquals(replaced, competencia);
        }

        @Test
        void testReplaceCompetenciaAsociadaCompetenciaNula() throws EntityNotFoundException {
            ResultadoFinalEntity resultadoFinal = resultadosFinalesList.get(0);
            CompetenciaEntity competenciaNula = null;
            
            assertThrows(IllegalOperationException.class, () -> {
                resultadoFinalCompetenciaService.replaceCompetenciaAsociada(resultadoFinal.getId(), competenciaNula);
            });
        }

        @Test
        void testReplaceCompetenciaAsociadaResultadoExistente() throws EntityNotFoundException {
            // Se crea un objeto CompetenciaEntity para usarlo como remplazo
            CompetenciaEntity competencia = competenciasList.get(0);
        
            // Se utiliza el id de un ResultadoFinalEntity existente
            Long resultadoId = resultadosFinalesList.get(0).getId();
        
            // Se verifica que no se lanza una EntityNotFoundException al intentar reemplazar la competencia asociada
            assertDoesNotThrow(() -> {
                resultadoFinalCompetenciaService.replaceCompetenciaAsociada(resultadoId, competencia);
            });
        }    

        @Test
        void testRemoveCompetenciaAsociadaWithInvalidResultado() {
            assertThrows(EntityNotFoundException.class, () -> {
                resultadoFinalCompetenciaService.removeCompetenciaAsociada(0L);
            });
        }


}

        

