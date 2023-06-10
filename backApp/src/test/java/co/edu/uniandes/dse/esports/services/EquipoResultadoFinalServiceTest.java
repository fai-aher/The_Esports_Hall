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
import co.edu.uniandes.dse.esports.repositories.ResultadoFinalRepository;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EquipoResultadoFinalService.class)
class EquipoResultadoFinalServiceTest {


        //En cada clase se inyecta su servicio y el Entity Manager.
        @Autowired
        private EquipoResultadoFinalService equipoResultadoFinalService;

        @Autowired
        private ResultadoFinalRepository resultadoFinalRepository;
    
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
    
        void testGetResultadosFinales() throws EntityNotFoundException {
        
            EquipoEntity equipoPrueba = equiposList.get(0);
    
            List<ResultadoFinalEntity> resultadosFinalesPrueba = equipoPrueba.getResultadosEnCompetencias();
    
            List<ResultadoFinalEntity> resultadosFinalesEntities = equipoResultadoFinalService.getResultadoFinalEntities(equipoPrueba.getId());
    
            assertNotNull(resultadosFinalesEntities);
    
            assertEquals(resultadosFinalesPrueba.size(), resultadosFinalesEntities.size());
        
            for (int i = 0; i < resultadosFinalesPrueba.size(); i++) {
    
                    ResultadoFinalEntity prueba1 = resultadosFinalesPrueba.get(i);
                    ResultadoFinalEntity prueba2 = resultadosFinalesEntities.get(i);
    
    
                    assertEquals(prueba1.getId(), prueba2.getId());
                    assertEquals(prueba1.getCompetenciaRelacionada(), prueba2.getCompetenciaRelacionada());
                    assertEquals(prueba1.getEquipoInvolucrado(), prueba2.getEquipoInvolucrado());
                    assertEquals(prueba1.getPosicionFinal(), prueba2.getPosicionFinal());
                    assertEquals(prueba1.getParteDeEmpate(), prueba2.getParteDeEmpate());
    
            }
    
        }
    
        @Test
        void testGetResultadoFinalEntitiesWithInvalidEquipo() {
            assertThrows(EntityNotFoundException.class, () -> {
                Long equipoId = -1L;
                equipoResultadoFinalService.getResultadoFinalEntities(equipoId);
            });
        }

    
        @Test
        // Test get jugador integrante
    
        void testGetResultadoFinalDeCompetencia() throws EntityNotFoundException, IllegalOperationException {
        
            EquipoEntity equipoPrueba = equiposList.get(0);

            CompetenciaEntity competenciaPrueba = competenciasList.get(0);

            List<ResultadoFinalEntity> resultadosParaAniadir = new ArrayList<>();

            ResultadoFinalEntity newResultado = factory.manufacturePojo(ResultadoFinalEntity.class);

            newResultado.setCompetenciaRelacionada(competenciaPrueba);

            entityManager.persist(newResultado);

            resultadosParaAniadir.add(newResultado);
            
            equipoPrueba.setResultadosEnCompetencias(resultadosParaAniadir);
    
            List<ResultadoFinalEntity> resultadosFinalesPrueba = equipoPrueba.getResultadosEnCompetencias();

            ResultadoFinalEntity resultadoFinalPrueba = resultadosFinalesPrueba.get(0);

            CompetenciaEntity competenciaRelacionada = resultadoFinalPrueba.getCompetenciaRelacionada();
    
            ResultadoFinalEntity resultadoFinalEntity = equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipoPrueba.getId(), competenciaRelacionada.getId());
    
            assertNotNull(resultadoFinalEntity);
       
    
            ResultadoFinalEntity prueba1 = resultadoFinalPrueba;
            ResultadoFinalEntity prueba2 = resultadoFinalEntity;
    
    
            assertEquals(prueba1.getId(), prueba2.getId());
            assertEquals(prueba1.getCompetenciaRelacionada(), prueba2.getCompetenciaRelacionada());
            assertEquals(prueba1.getEquipoInvolucrado(), prueba2.getEquipoInvolucrado());
            assertEquals(prueba1.getPosicionFinal(), prueba2.getPosicionFinal());
            assertEquals(prueba1.getParteDeEmpate(), prueba2.getParteDeEmpate());
    
            }

        @Test
        void testGetResultadoDeUnaCompetenciaEquipoNoEncontrado() {
        //Ejecutar el método a probar con un id de equipo que no existe
        assertThrows(EntityNotFoundException.class, () -> equipoResultadoFinalService.getResultadoDeUnaCompetencia(0L, 1L));
        }

        @Test
        void testGetResultadoDeUnaCompetenciaCompetenciaNoEncontrada() {
        //Crear un equipo y persistirlo
        EquipoEntity equipo = factory.manufacturePojo(EquipoEntity.class);
        entityManager.persist(equipo);

        //Ejecutar el método a probar con un id de competencia que no existe
        assertThrows(EntityNotFoundException.class, () -> equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipo.getId(), 0L));
        }

        @Test
        void testGetResultadoDeUnaCompetenciaResultadoNoEncontrado() {
        //Crear un equipo y persistirlo
        EquipoEntity equipo = factory.manufacturePojo(EquipoEntity.class);
        entityManager.persist(equipo);

        //Crear una competencia y persistirla
        CompetenciaEntity competencia = factory.manufacturePojo(CompetenciaEntity.class);
        entityManager.persist(competencia);

        //Ejecutar el método a probar con un equipo y competencia existentes, pero sin resultado final asociado
        assertThrows(IllegalOperationException.class, () -> equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipo.getId(), competencia.getId()));
        }
        
        @Test
        void testGetResultadoDeUnaCompetenciaEquipoSinResultadosFinales() {

        assertThrows(EntityNotFoundException.class, () -> {
                Long equipoId = 0L; // id inexistente
                Long competenciaId = 1L; // id cualquiera

                equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipoId, competenciaId);
            });
        }

        @Test
        void testGetResultadoDeUnaCompetenciaSinRelacion() {
        assertThrows(EntityNotFoundException.class, () -> {
                Long equipoId = 1L; // id de un equipo que sí tiene resultados finales
                Long competenciaId = 0L; // id inexistente

                equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipoId, competenciaId);
            });
        }

        @Test
        void testGetResultadoDeUnaCompetenciaWithInvalidEquipo() {
            assertThrows(EntityNotFoundException.class, () -> {
            equipoResultadoFinalService.getResultadoDeUnaCompetencia(0L, competenciasList.get(0).getId());
            });
        }

        @Test
        void testGetResultadoDeUnaCompetenciaWithInvalidCompetencia() {
            assertThrows(EntityNotFoundException.class, () -> {
            EquipoEntity equipo = equiposList.get(0);
            equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipo.getId(), 0L);
            });
        }

        @Test
        void testGetResultadoDeUnaCompetenciaWithEquipoNotParticipated() {
            assertThrows(IllegalOperationException.class, () -> {
            EquipoEntity equipo = equiposList.get(0);
            CompetenciaEntity competencia = competenciasList.get(0);
            equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipo.getId(), competencia.getId());
            });
        }

        @Test
        void testGetResultadoDeUnaCompetenciaWithValidData() throws EntityNotFoundException, IllegalOperationException{
            EquipoEntity equipo = equiposList.get(0);
            CompetenciaEntity competencia = competenciasList.get(2);
            ResultadoFinalEntity resultadoFinal = new ResultadoFinalEntity();
            resultadoFinal.setCompetenciaRelacionada(competencia);
            resultadoFinal.setEquipoInvolucrado(equipo);
            resultadoFinal.setPosicionFinal(2);

            List<ResultadoFinalEntity> lista = new ArrayList<>();
            lista.add(0, resultadoFinal);

            equipo.setResultadosEnCompetencias(lista);
            ResultadoFinalEntity resultadoObtenido = equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipo.getId(), competencia.getId());
            assertEquals(resultadoFinal, resultadoObtenido);
        }

        @Test
        void testGetResultadoDeUnaCompetenciaWithInvalidCompetenciaId() {
            assertThrows(EntityNotFoundException.class, () -> {
                EquipoEntity equipo = equiposList.get(0);
                equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipo.getId(), 0L);
            });
        }
        
        @Test
        void testGetResultadoDeUnaCompetenciaWithInvalidEquipoId() {
            assertThrows(EntityNotFoundException.class, () -> {
                CompetenciaEntity competencia = competenciasList.get(0);
                equipoResultadoFinalService.getResultadoDeUnaCompetencia(0L, competencia.getId());
            });
        }
        
        @Test
        void testGetResultadoDeUnaCompetenciaWithNoResultados() {
            assertThrows(IllegalOperationException.class, () -> {
                EquipoEntity equipo = equiposList.get(0);
                CompetenciaEntity competencia = competenciasList.get(0);
                equipoResultadoFinalService.getResultadoDeUnaCompetencia(equipo.getId(), competencia.getId());
            });
        }

        @Test
        void testGetResultadoDeUnaCompetenciaEquipoInvalido() throws EntityNotFoundException, IllegalOperationException {
    
            // Obtener una competencia y un equipo
            CompetenciaEntity competencia = competenciasList.get(0);
            EquipoEntity equipo = equiposList.get(0);
    
            // Agregar un resultado final a un equipo
            ResultadoFinalEntity resultadoFinal = new ResultadoFinalEntity();
            resultadoFinal.setCompetenciaRelacionada(competencia);
            resultadoFinal.setEquipoInvolucrado(equipo);
            resultadoFinalRepository.save(resultadoFinal);
            equipo.getResultadosEnCompetencias().add(resultadoFinal);
    
            // Intentar obtener el resultado final con un equipo diferente
            EquipoEntity otroEquipo = equiposList.get(1);
            assertThrows(IllegalOperationException.class, () -> {
                equipoResultadoFinalService.getResultadoDeUnaCompetencia(otroEquipo.getId(), competencia.getId());
            });
        }
        

            
}
