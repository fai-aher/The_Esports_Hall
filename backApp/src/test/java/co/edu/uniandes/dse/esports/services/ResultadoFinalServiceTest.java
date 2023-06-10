package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.constraints.AssertTrue;

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
import co.edu.uniandes.dse.esports.repositories.ResultadoFinalRepository;
import co.edu.uniandes.dse.esports.entities.EquipoEntity;
import co.edu.uniandes.dse.esports.entities.CompetenciaEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ResultadoFinalService.class)
class ResultadoFinalServiceTest {
    
    //En cada clase se inyecta su servicio y el Entity Manager.
    @Autowired
    private ResultadoFinalService resultadoFinalService;

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


    //Pruebas de los 5 metodos CRUD.

    @Test
    void testCreateResultadoFinal() throws EntityNotFoundException, IllegalOperationException {

        ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

        newEntity.setId(4L);
        newEntity.setCompetenciaRelacionada(competenciasList.get(0));
        newEntity.setEquipoInvolucrado(equiposList.get(0));
        newEntity.setParteDeEmpate(false);
        
        ResultadoFinalEntity result = resultadoFinalService.createResultadoFinal(newEntity);

        //Assert
        assertNotNull(result);

        ResultadoFinalEntity entity = entityManager.find(ResultadoFinalEntity.class, result.getId());

        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getCompetenciaRelacionada(), entity.getCompetenciaRelacionada());
        assertEquals(newEntity.getEquipoInvolucrado(), entity.getEquipoInvolucrado());
        assertEquals(newEntity.getParteDeEmpate(), entity.getParteDeEmpate());
        assertEquals(newEntity.getPosicionFinal(), entity.getPosicionFinal());

    }


    @Test
    void testCreateResultadoCompetenciaNull() {

        assertThrows(IllegalOperationException.class, () -> {
            ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

            EquipoEntity equipoPrueba = equiposList.get(0);

            newEntity.setEquipoInvolucrado(equipoPrueba);
            newEntity.setCompetenciaRelacionada(null);
            newEntity.setParteDeEmpate(false);

            resultadoFinalService.createResultadoFinal(newEntity);
        });
  
    }

    @Test
    void testCreateResultadoEquipoNull() {

        assertThrows(IllegalOperationException.class, () -> {
            ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

            CompetenciaEntity competenciaPrueba = competenciasList.get(0);

            newEntity.setEquipoInvolucrado(null);
            newEntity.setCompetenciaRelacionada(competenciaPrueba);
            newEntity.setParteDeEmpate(false);

            resultadoFinalService.createResultadoFinal(newEntity);
        });
  
    }

    @Test
    void testCreateResultadoPosicionNull() {

        assertThrows(IllegalOperationException.class, () -> {
            ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

            CompetenciaEntity competenciaPrueba = competenciasList.get(0);
            EquipoEntity equipoPrueba = equiposList.get(0);

            newEntity.setEquipoInvolucrado(equipoPrueba);
            newEntity.setCompetenciaRelacionada(competenciaPrueba);
            newEntity.setParteDeEmpate(false);
            newEntity.setPosicionFinal(null);

            resultadoFinalService.createResultadoFinal(newEntity);
        });
  
    }


    @Test
    void testCreateResultadoEmpateNull() {

        assertThrows(IllegalOperationException.class, () -> {
            ResultadoFinalEntity newEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

            CompetenciaEntity competenciaPrueba = competenciasList.get(0);
            EquipoEntity equipoPrueba = equiposList.get(0);

            newEntity.setEquipoInvolucrado(equipoPrueba);
            newEntity.setCompetenciaRelacionada(competenciaPrueba);
            newEntity.setParteDeEmpate(null);

            resultadoFinalService.createResultadoFinal(newEntity);
        });
  
    }    


    @Test
    void testGetAllResultadosFinales() {

        List<ResultadoFinalEntity> listaPrueba = resultadoFinalService.getAllResultadosFinales();
        assertEquals(resultadosFinalesList.size(), listaPrueba.size());

        for (ResultadoFinalEntity entity : listaPrueba) {
            boolean found = false;
            for (ResultadoFinalEntity storedEntity : resultadosFinalesList) {
                if (entity.getId() == (storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetResultadoFinal() throws EntityNotFoundException {

        ResultadoFinalEntity entity = resultadosFinalesList.get(0);
        ResultadoFinalEntity resultEntity  = resultadoFinalService.getResultadoFinal(entity.getId());

        assertNotNull(resultEntity);

        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getCompetenciaRelacionada(), resultEntity.getCompetenciaRelacionada());
        assertEquals(entity.getEquipoInvolucrado(), resultEntity.getEquipoInvolucrado());
        assertEquals(entity.getParteDeEmpate(), resultEntity.getParteDeEmpate());
        assertEquals(entity.getPosicionFinal(), resultEntity.getPosicionFinal());

    }


    @Test
    void testGetInvalidResultado() {
        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalService.getResultadoFinal(0L);
        });

    }


    @Test
    void testUpdateResultadoFinal() throws EntityNotFoundException, IllegalOperationException {
        ResultadoFinalEntity entity = resultadosFinalesList.get(0);
        ResultadoFinalEntity pojoEntity = factory.manufacturePojo(ResultadoFinalEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setCompetenciaRelacionada(competenciasList.get(0));
        pojoEntity.setEquipoInvolucrado(equiposList.get(0));

        resultadoFinalService.updateResultadoFinal(entity.getId(), pojoEntity);

        ResultadoFinalEntity resp = entityManager.find(ResultadoFinalEntity.class, entity.getId());

        assertEquals(entity.getId(), resp.getId());
        assertEquals(entity.getCompetenciaRelacionada(), resp.getCompetenciaRelacionada());
        assertEquals(entity.getEquipoInvolucrado(), resp.getEquipoInvolucrado());
        assertEquals(entity.getParteDeEmpate(), resp.getParteDeEmpate());
        assertEquals(entity.getPosicionFinal(), resp.getPosicionFinal());

    }


    @Test
    void testUpdateInvalidResultadoFinal() {

        assertThrows(EntityNotFoundException.class, () -> {

            ResultadoFinalEntity pojoEntity = factory.manufacturePojo(ResultadoFinalEntity.class);
            pojoEntity.setId(0L);
            resultadoFinalService.updateResultadoFinal(0L, pojoEntity);

        });

    }

    @Test
    void testUpdateResultadoWithNullCompetencia() {

        assertThrows(IllegalOperationException.class, () -> {

            ResultadoFinalEntity entity = resultadosFinalesList.get(0);
            ResultadoFinalEntity pojoEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

            pojoEntity.setCompetenciaRelacionada(null);
            pojoEntity.setEquipoInvolucrado(equiposList.get(0));
            pojoEntity.setId(entity.getId());
            resultadoFinalService.updateResultadoFinal(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testUpdateResultadoWithNullEquipo() {

        assertThrows(IllegalOperationException.class, () -> {

            ResultadoFinalEntity entity = resultadosFinalesList.get(0);
            ResultadoFinalEntity pojoEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

            pojoEntity.setCompetenciaRelacionada(competenciasList.get(0));
            pojoEntity.setEquipoInvolucrado(null);
            pojoEntity.setId(entity.getId());
            resultadoFinalService.updateResultadoFinal(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testUpdateResultadoWithNullPosicion() {

        assertThrows(IllegalOperationException.class, () -> {

            ResultadoFinalEntity entity = resultadosFinalesList.get(0);
            ResultadoFinalEntity pojoEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

            pojoEntity.setCompetenciaRelacionada(competenciasList.get(0));
            pojoEntity.setEquipoInvolucrado(equiposList.get(0));
            pojoEntity.setPosicionFinal(null);
            pojoEntity.setId(entity.getId());
            resultadoFinalService.updateResultadoFinal(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testUpdateResultadoWithNullEmpate() {

        assertThrows(IllegalOperationException.class, () -> {

            ResultadoFinalEntity entity = resultadosFinalesList.get(0);
            ResultadoFinalEntity pojoEntity = factory.manufacturePojo(ResultadoFinalEntity.class);

            pojoEntity.setCompetenciaRelacionada(competenciasList.get(0));
            pojoEntity.setEquipoInvolucrado(equiposList.get(0));
            pojoEntity.setParteDeEmpate(null);
            pojoEntity.setId(entity.getId());
            resultadoFinalService.updateResultadoFinal(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testDeleteResultadoFinal() throws EntityNotFoundException, IllegalOperationException {

        ResultadoFinalEntity entity = resultadosFinalesList.get(1);

        entity.setCompetenciaRelacionada(null);
        entity.setEquipoInvolucrado(null);

        resultadoFinalService.deleteResultadoFinal(entity.getId());
        
        ResultadoFinalEntity deleted = entityManager.find(ResultadoFinalEntity.class, entity.getId());

        assertNull(deleted);

    }


    @Test
    void testDeleteInvalidResultadoFinal() {

        assertThrows(EntityNotFoundException.class, () -> {

            resultadoFinalService.deleteResultadoFinal(0L);
        });

    }
    
    @Test
    void testDeleteResultadoWithCompetencia() throws EntityNotFoundException, IllegalOperationException{

            ResultadoFinalEntity entity = resultadosFinalesList.get(0);
            entity.setCompetenciaRelacionada(competenciasList.get(0));
            entity.setEquipoInvolucrado(null);

            assertThrows(IllegalOperationException.class, () -> {
            resultadoFinalService.deleteResultadoFinal(entity.getId());
        });
    }

    @Test
    void testDeleteResultadoWithEquipoAndCompetencia() {

        assertThrows(IllegalOperationException.class, () -> {

            ResultadoFinalEntity resultadoPrueba = resultadosFinalesList.get(0);

            resultadoPrueba.setCompetenciaRelacionada(competenciasList.get(1));
            resultadoPrueba.setEquipoInvolucrado(equiposList.get(1));

            resultadoFinalService.deleteResultadoFinal(resultadoPrueba.getId());

        });

    }

    @Test
    void testDeleteResultadoFinalWithCompetenciaVinculada() {
        assertThrows(IllegalOperationException.class, () -> {
            ResultadoFinalEntity resultado = resultadosFinalesList.get(0);
            resultado.setCompetenciaRelacionada(new CompetenciaEntity());
            resultadoFinalService.deleteResultadoFinal(resultado.getId());
        });
    }

    @Test
    void testDeleteResultadoFinalWithEquipoVinculado() {
        assertThrows(IllegalOperationException.class, () -> {
            ResultadoFinalEntity resultado = resultadosFinalesList.get(0);
            resultado.setEquipoInvolucrado(new EquipoEntity());
            resultadoFinalService.deleteResultadoFinal(resultado.getId());
        });
    }

    @Test
    void testDeleteNonexistentResultadoFinal() {
        assertThrows(EntityNotFoundException.class, () -> {
            resultadoFinalService.deleteResultadoFinal(0L);
        });
    }

    @Test
    void testDeleteResultadoFinalNoEquipoAsociado() throws EntityNotFoundException, IllegalOperationException {
        // crear el resultado final sin equipo asociado
        ResultadoFinalEntity resultado = new ResultadoFinalEntity();
        resultado.setEquipoInvolucrado(null);
        resultado.setCompetenciaRelacionada(null);
        resultado = resultadoFinalRepository.save(resultado);
    
        // asegurarse que el resultado existe
        Optional<ResultadoFinalEntity> resultadoBuscado = resultadoFinalRepository.findById(resultado.getId());
        assertTrue(resultadoBuscado.isPresent());
    
        // borrar el resultado final
        resultadoFinalService.deleteResultadoFinal(resultado.getId());
    
        // asegurarse que el resultado fue borrado
        resultadoBuscado = resultadoFinalRepository.findById(resultado.getId());
        assertFalse(resultadoBuscado.isPresent());
    } 

    @Test
    void testDeleteResultadoFinalWithoutEquipoAsociado() throws EntityNotFoundException, IllegalOperationException {
        // Crear una competencia sin equipos ni resultados finales asociados
        CompetenciaEntity competencia = competenciasList.get(0);
        List<EquipoEntity> equiposVacios = new ArrayList<>();
        competencia.setEquiposParticipantes(equiposVacios);

        List<ResultadoFinalEntity> resultadosVacios = new ArrayList<>();
        competencia.setResultadosFinales(resultadosVacios);

        // Crear un resultado final sin equipos asociados
        ResultadoFinalEntity resultado = resultadosFinalesList.get(0);
        resultado.setEquipoInvolucrado(null);
        resultado.setCompetenciaRelacionada(null);

        // Borrar el resultado final
        resultadoFinalService.deleteResultadoFinal(resultado.getId());

        // Verificar que el resultado final fue borrado correctamente
        Optional<ResultadoFinalEntity> resultadoBorrado = resultadoFinalRepository.findById(resultado.getId());
        assertTrue(resultadoBorrado.isEmpty());
    }

    @Test
    void testDeleteResultadoFinalWithEquipoAsociado() throws EntityNotFoundException, IllegalOperationException {
        // Crear una competencia sin equipos ni resultados finales asociados
        CompetenciaEntity competencia = competenciasList.get(0);
        List<EquipoEntity> equiposVacios = new ArrayList<>();
        competencia.setEquiposParticipantes(equiposVacios);

        List<ResultadoFinalEntity> resultadosVacios = new ArrayList<>();
        competencia.setResultadosFinales(resultadosVacios);

        // Crear un resultado final sin equipos asociados
        ResultadoFinalEntity resultado = resultadosFinalesList.get(0);
        resultado.setEquipoInvolucrado(equiposList.get(0));
        resultado.setCompetenciaRelacionada(null);

        // Borrar el resultado final

        assertThrows(IllegalOperationException.class, () -> {

            resultadoFinalService.deleteResultadoFinal(resultado.getId());
        });

    }
    

}
