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
import co.edu.uniandes.dse.esports.repositories.ResultadoFinalRepository;
import co.edu.uniandes.dse.esports.entities.ResultadoFinalEntity;
import co.edu.uniandes.dse.esports.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.esports.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ResultadoFinalEquipoService.class)
class ResultadoFinalEquipoServiceTest {

    @Autowired
    private ResultadoFinalEquipoService resultadoFinalEquipoService;

    @Autowired
    private ResultadoFinalRepository resultadoFinalRepository;

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
void testAddEquipoInvolucrado() throws EntityNotFoundException, IllegalOperationException {

        EquipoEntity newEquipo = equiposList.get(0);
        
        ResultadoFinalEntity newResultado = resultadosFinalesList.get(0);
        
        resultadoFinalEquipoService.addEquipoInvolucrado(newResultado.getId(), newEquipo.getId());
        
        EquipoEntity lastResultado = resultadoFinalEquipoService.getEquipoInvolucrado(newResultado.getId());

        EquipoEntity prueba1 = newEquipo;
        EquipoEntity prueba2 = lastResultado;

        assertEquals(prueba1.getId(), prueba2.getId());
        assertEquals(prueba1.getNombre(), prueba2.getNombre());
        assertEquals(prueba1.getPaisProcedencia(), prueba2.getPaisProcedencia());
        assertEquals(prueba1.getBanderaPais(), prueba2.getBanderaPais());
        assertEquals(prueba1.getIntegrantes(), prueba2.getIntegrantes());
        assertEquals(prueba1.getCompetenciasParticipadas(), prueba2.getCompetenciasParticipadas());
        assertEquals(prueba1.getCompetenciasGanadas(), prueba2.getCompetenciasGanadas());


    }

    @Test
    void testGetEquipoInvolucrado() throws EntityNotFoundException {

        ResultadoFinalEntity resultadoFinalEntity = resultadosFinalesList.get(0);

        EquipoEntity equipoPrueba = equiposList.get(0);

        resultadoFinalEntity.setEquipoInvolucrado(equipoPrueba);
        
        EquipoEntity equipoEntity = resultadoFinalEquipoService.getEquipoInvolucrado(resultadoFinalEntity.getId());

        assertNotNull(equipoEntity);
    
        EquipoEntity prueba1 = equipoPrueba;
        EquipoEntity prueba2 = equipoEntity;

        assertEquals(prueba1.getId(), prueba2.getId());
        assertEquals(prueba1.getNombre(), prueba2.getNombre());
        assertEquals(prueba1.getPaisProcedencia(), prueba2.getPaisProcedencia());
        assertEquals(prueba1.getBanderaPais(), prueba2.getBanderaPais());
        assertEquals(prueba1.getIntegrantes(), prueba2.getIntegrantes());
        assertEquals(prueba1.getCompetenciasParticipadas(), prueba2.getCompetenciasParticipadas());
        assertEquals(prueba1.getCompetenciasGanadas(), prueba2.getCompetenciasGanadas());
    }


    @Test
    void testAddInvalidEquipo() {

            assertThrows(EntityNotFoundException.class , () -> {
                    ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);
                    entityManager.persist(newEntity);

                    resultadoFinalEquipoService.addEquipoInvolucrado(newEntity.getId(), 0L);
        
                });


    }

    @Test
    void testAddNullEquipo() {

            assertThrows(IllegalOperationException.class , () -> {
                    ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);
                    entityManager.persist(newEntity);

                    resultadoFinalEquipoService.addEquipoInvolucrado(newEntity.getId(), null);
        
                });


    }

    //Para el Add
    
    @Test
    void testAddEquipoInvolucradoEquipoNoExistente() throws EntityNotFoundException, IllegalOperationException {
        ResultadoFinalEntity resultadoFinal= resultadosFinalesList.get(0);
        EquipoEntity equipo = factory.manufacturePojo(EquipoEntity.class);
        equipo.setId(0L);
    
        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalEquipoService.addEquipoInvolucrado(resultadoFinal.getId(), equipo.getId());
        });
    }
    
    @Test
    void testAddEquipoInvolucradoResultadoFinalNoExistente() throws EntityNotFoundException, IllegalOperationException {
        EquipoEntity equipo = equiposList.get(0);
        ResultadoFinalEntity resultadoFinal = factory.manufacturePojo(ResultadoFinalEntity.class);
        resultadoFinal.setId(0L);
    
        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalEquipoService.addEquipoInvolucrado(resultadoFinal.getId(), equipo.getId());
        });
    }

    //Get Equipo Involucrado

    @Test
    void testGetEquipoInvolucradoResultadoNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalEquipoService.getEquipoInvolucrado(0L);
        });
    }

    //Tests para remplazar el equipo involucrado

    @Test
    void replaceEquipoInvolucradoTest() throws EntityNotFoundException {
        ResultadoFinalEntity resultado = resultadosFinalesList.get(0);
        EquipoEntity equipo = equiposList.get(1);

        EquipoEntity result = resultadoFinalEquipoService.replaceEquipoInvolucrado(resultado.getId(), equipo);

        assertEquals(equipo, result);
        assertEquals(equipo, resultadoFinalEquipoService.getEquipoInvolucrado(resultado.getId()));
    }


    @Test
    void testReplaceEquipoInvolucradoWithSameEquipo() throws EntityNotFoundException {
        ResultadoFinalEntity resultado = resultadosFinalesList.get(0);
        EquipoEntity equipoAnterior = resultado.getEquipoInvolucrado();
    
        resultadoFinalEquipoService.replaceEquipoInvolucrado(resultado.getId(), equipoAnterior);
    
        EquipoEntity equipoActual = resultadoFinalEquipoService.getEquipoInvolucrado(resultado.getId());
    
        assertEquals(equipoAnterior, equipoActual);
    }
    
    @Test
    void testReplaceEquipoInvolucradoWithNonexistentEquipo() {
        ResultadoFinalEntity resultado = resultadosFinalesList.get(0);
        EquipoEntity equipoNuevo = factory.manufacturePojo(EquipoEntity.class);
        equipoNuevo.setId(0L);
    
        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalEquipoService.replaceEquipoInvolucrado(resultado.getId(), equipoNuevo);
        });
    }
    
    @Test
    void testReplaceEquipoInvolucradoWithNonexistentResultado() {
        ResultadoFinalEntity resultado = factory.manufacturePojo(ResultadoFinalEntity.class);
        EquipoEntity equipoNuevo = factory.manufacturePojo(EquipoEntity.class);
        resultado.setId(0L);
    
        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalEquipoService.replaceEquipoInvolucrado(resultado.getId(), equipoNuevo);
        });
    }

    // Pruebas unitarias para removeEquipoInvolucrado

    @Test
    void testRemoveEquipoInvolucrado() throws EntityNotFoundException {
        ResultadoFinalEntity resultado = resultadoFinalRepository.save(factory.manufacturePojo(ResultadoFinalEntity.class));
        Long resultadoId = resultado.getId();
        EquipoEntity equipo = equiposList.get(0);
        resultado.setEquipoInvolucrado(equipo);
        resultadoFinalRepository.save(resultado);
    
        resultadoFinalEquipoService.removeEquipoInvolucrado(resultadoId);
    
        ResultadoFinalEntity resultadoActualizado = resultadoFinalRepository.findById(resultadoId)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado el resultado final con ese id."));
        assertNull(resultadoActualizado.getEquipoInvolucrado());
    }
    
    @Test
    void testRemoveEquipoInvolucradoInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> resultadoFinalEquipoService.removeEquipoInvolucrado(0L));
    }

}

    
