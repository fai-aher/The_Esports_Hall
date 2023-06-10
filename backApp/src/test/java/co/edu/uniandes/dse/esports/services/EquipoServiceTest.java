package co.edu.uniandes.dse.esports.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EquipoService.class)

class EquipoServiceTest {

    private static final String MENSAJEBANDERA = "El equipo debe tener una bandera de un pais asociada.";
    private static final String MENSAJENOMBRE = "El equipo debe tener un nombre asociado.";
    
    //En cada clase se inyecta su servicio y el Entity Manager.
    @Autowired
    private EquipoService equipoService;

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

    //Pruebas de los 5 metodos CRUD.

    @Test
    void testCreateEquipo() throws EntityNotFoundException, IllegalOperationException {

        EquipoEntity newEntity = factory.manufacturePojo(EquipoEntity.class);

        newEntity.setId(4L);
        newEntity.setIntegrantes(jugadoresList);
        newEntity.setCompetenciasParticipadas(competenciasList);
        
        EquipoEntity result = equipoService.createEquipo(newEntity);
        //Assert
        assertNotNull(result);

        EquipoEntity entity = entityManager.find(EquipoEntity.class, result.getId());

        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getCompetenciasParticipadas(), entity.getCompetenciasParticipadas());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getPaisProcedencia(), entity.getPaisProcedencia());
        assertEquals(newEntity.getCompetenciasGanadas(), entity.getCompetenciasGanadas());
        assertEquals(newEntity.getIntegrantes(), entity.getIntegrantes());
        assertEquals(newEntity.getLogo(), entity.getLogo());
        assertEquals(newEntity.getTorneosParticipados(), entity.getTorneosParticipados());

    }

    @Test
    void testCreateEquipoCompetenciasNull() {

        assertThrows(IllegalOperationException.class, () -> {
            EquipoEntity equipoPrueba = equiposList.get(0);
            EquipoEntity newEntity = factory.manufacturePojo(EquipoEntity.class);
            newEntity.setId(equipoPrueba.getId());

            newEntity.setIntegrantes(jugadoresList);
            newEntity.setCompetenciasParticipadas(null);

            equipoService.createEquipo(newEntity);
        });
  
    }

    @Test
    void testCreateEquipoCeroCompetencias() {

        assertThrows(IllegalOperationException.class, () -> {
            EquipoEntity equipoPrueba = equiposList.get(0);
            EquipoEntity newEntity = factory.manufacturePojo(EquipoEntity.class);
            newEntity.setId(equipoPrueba.getId());

            List<CompetenciaEntity> ceroCompetencias = new ArrayList<>();

            newEntity.setIntegrantes(jugadoresList);
            newEntity.setCompetenciasParticipadas(ceroCompetencias);

            equipoService.createEquipo(newEntity);
        });
  
    }

    @Test
    void testGetAllEquipos() {

        List<EquipoEntity> listaPrueba = equipoService.getAllEquipos();
        assertEquals(equiposList.size(), listaPrueba.size());

        for (EquipoEntity entity : listaPrueba) {
            boolean found = false;
            for (EquipoEntity storedEntity : equiposList) {
                if (entity.getId() == (storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetEquipo() throws EntityNotFoundException {

        EquipoEntity entity = equiposList.get(0);
        EquipoEntity resultEntity  = equipoService.getEquipo(entity.getId());

        assertNotNull(resultEntity);

        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getCompetenciasParticipadas(), resultEntity.getCompetenciasParticipadas());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getPaisProcedencia(), resultEntity.getPaisProcedencia());
        assertEquals(entity.getCompetenciasGanadas(), resultEntity.getCompetenciasGanadas());
        assertEquals(entity.getIntegrantes(), resultEntity.getIntegrantes());
        assertEquals(entity.getLogo(), resultEntity.getLogo());
        assertEquals(entity.getTorneosParticipados(), resultEntity.getTorneosParticipados());

    }

    @Test
    void testGetInvalidEquipo() {
        assertThrows(EntityNotFoundException.class, () -> {
            equipoService.getEquipo(0L);
        });

    }


    @Test
    void testUpdateEquipo() throws EntityNotFoundException, IllegalOperationException {
        EquipoEntity entity = equiposList.get(0);
        EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setCompetenciasParticipadas(competenciasList);
        equipoService.updateEquipo(entity.getId(), pojoEntity);

        EquipoEntity resp = entityManager.find(EquipoEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getCompetenciasParticipadas(), resp.getCompetenciasParticipadas());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getPaisProcedencia(), resp.getPaisProcedencia());
        assertEquals(pojoEntity.getCompetenciasGanadas(), resp.getCompetenciasGanadas());
        assertEquals(pojoEntity.getIntegrantes(), resp.getIntegrantes());
        assertEquals(pojoEntity.getLogo(), resp.getLogo());
        assertEquals(pojoEntity.getTorneosParticipados(), resp.getTorneosParticipados());

    }

    @Test
    void testUpdateInvalidEquipo() {

        assertThrows(EntityNotFoundException.class, () -> {
            EquipoEntity entity = equiposList.get(0);
            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
            pojoEntity.setId(entity.getId());
            pojoEntity.setCompetenciasParticipadas(competenciasList);
            equipoService.updateEquipo(0L, pojoEntity);

        });

    }

    @Test
    void testUpdateEquipoWithNoCompetencias() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity entity = equiposList.get(0);
            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);

            List<CompetenciaEntity> competenciasVacias = new ArrayList<>();

            pojoEntity.setCompetenciasParticipadas(competenciasVacias);
            pojoEntity.setId(entity.getId());
            equipoService.updateEquipo(entity.getId(), pojoEntity);

        });
    }

    @Test
    void testUpdateEquipoWithNullCompetencias() {

        assertThrows(IllegalOperationException.class, () -> {
;
            EquipoEntity entity = equiposList.get(0);
            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);

            pojoEntity.setCompetenciasParticipadas(null);

            pojoEntity.setId(entity.getId());
            equipoService.updateEquipo(entity.getId(), pojoEntity);
                    });
    }

    @Test
    void testDeleteEquipo() throws EntityNotFoundException, IllegalOperationException {

        EquipoEntity entity = equiposList.get(1);
        
        //Configuracion de una lista con 0 competenciasParticipadas para habilitar la delecion del equipo.
        List<CompetenciaEntity> ceroCompetencias = new ArrayList<>();
        entity.setCompetenciasParticipadas(ceroCompetencias);

        equipoService.deleteEquipo(entity.getId());
        
        EquipoEntity deleted = entityManager.find(EquipoEntity.class, entity.getId());

        assertNull(deleted);

    }

    @Test
    void testDeleteInvalidEquipo() {

        assertThrows(EntityNotFoundException.class, () -> {

            equipoService.deleteEquipo(0L);
        });

    }
    
    @Test
    void testDeleteEquipoWithCompetencias() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity entity = equiposList.get(0);
            entity.setCompetenciasParticipadas(competenciasList);

            equipoService.deleteEquipo(entity.getId());
        });
    }


    @Test
    void createEquipoWithNullBandera() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity entity = equiposList.get(0);
            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
            pojoEntity.setId(1L);
            pojoEntity.setCompetenciasParticipadas(competenciasList);
            pojoEntity.setId(entity.getId());
            pojoEntity.setBanderaPais(null);

            equipoService.createEquipo(pojoEntity);


        });

    }

    @Test
    void createEquipoWithEmptyStringBandera() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity entity = equiposList.get(0);
            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
            pojoEntity.setId(1L);
            pojoEntity.setCompetenciasParticipadas(competenciasList);
            pojoEntity.setId(entity.getId());
            pojoEntity.setBanderaPais("");

            equipoService.createEquipo(pojoEntity);


        });

    }


    @Test
    void createEquipoWithNoLogo() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity entity = equiposList.get(0);
            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
            pojoEntity.setId(1L);
            pojoEntity.setCompetenciasParticipadas(competenciasList);
            pojoEntity.setId(entity.getId());
            pojoEntity.setLogo(null);

            equipoService.createEquipo(pojoEntity);


        });

    }

    @Test
    void createEquipoWithEmptyStringLogo() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity entity = equiposList.get(0);
            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
            pojoEntity.setId(1L);
            pojoEntity.setCompetenciasParticipadas(competenciasList);
            pojoEntity.setId(entity.getId());
            pojoEntity.setLogo("");

            equipoService.createEquipo(pojoEntity);


        });
  
    
}

@Test
void createEquipoWithNoNombre() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
        pojoEntity.setId(1L);
        pojoEntity.setCompetenciasParticipadas(competenciasList);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNombre(null);

        equipoService.createEquipo(pojoEntity);


    });

}

@Test
void createEquipoWithEmptyStringNombre() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
        pojoEntity.setId(1L);
        pojoEntity.setCompetenciasParticipadas(competenciasList);
        pojoEntity.setNombre("");

        equipoService.createEquipo(pojoEntity);


    });

}

    @Test
    void createEquipoWithNoPaisProcedencia() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
            pojoEntity.setId(1L);
            pojoEntity.setCompetenciasParticipadas(competenciasList);
            pojoEntity.setPaisProcedencia(null);

            equipoService.createEquipo(pojoEntity);


        });

    }

    @Test
    void createEquipoWithEmptyStringPaisProcedencia() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity pojoEntity = factory.manufacturePojo(EquipoEntity.class);
            pojoEntity.setId(1L);
            pojoEntity.setCompetenciasParticipadas(competenciasList);
            pojoEntity.setPaisProcedencia("");

            equipoService.createEquipo(pojoEntity);


        });

    }

    @Test
    void testCreateEquipoWithEmptyValues() {

        assertThrows(IllegalOperationException.class, () -> {

            EquipoEntity equipoPrueba = equiposList.get(0);
            equipoPrueba.setId(1L);
            equipoPrueba.setLogo("");
            equipoPrueba.setBanderaPais("");
            equipoPrueba.setNombre("");
            equipoPrueba.setPaisProcedencia("");

            equipoService.createEquipo(equipoPrueba);

        });

    }

// Nuevos test para alcanzar complet acobertura:

@Test
void createEquipoWithNullPaisProcedencia() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        EquipoEntity pojoEntity = new EquipoEntity();
        pojoEntity.setId(1L);
        pojoEntity.setBanderaPais(entity.getBanderaPais());
        pojoEntity.setCompetenciasParticipadas(competenciasList);
        pojoEntity.setId(entity.getId());
        pojoEntity.setLogo(entity.getLogo());
        pojoEntity.setNombre(entity.getNombre());
        pojoEntity.setPaisProcedencia(null);

        equipoService.createEquipo(pojoEntity);

    });
}

@Test
void updateEquipoWithNullBandera() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        entity.setBanderaPais(null);

        equipoService.updateEquipo(entity.getId(), entity);

    });
}

@Test
void updateEquipoWithEmptyStringBandera() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        entity.setBanderaPais("");

        equipoService.updateEquipo(entity.getId(), entity);

    });
}

@Test
void updateEquipoWithNullNombre() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        entity.setNombre(null);

        equipoService.updateEquipo(entity.getId(), entity);

    });
}

@Test
void updateEquipoWithEmptyStringNombre() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        entity.setNombre("");

        equipoService.updateEquipo(entity.getId(), entity);

    });
}

@Test
void updateEquipoWithEmptyStringLogo() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        entity.setLogo("");

        equipoService.updateEquipo(entity.getId(), entity);

    });
}

@Test
void updateEquipoWithNullogo() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        entity.setLogo(null);

        equipoService.updateEquipo(entity.getId(), entity);

    });
}

@Test
void updateEquipoWithNullPaisProcedencia() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        entity.setPaisProcedencia(null);

        equipoService.updateEquipo(entity.getId(),entity);

    });
}

@Test
void updateEquipoWithEmptyStringPaisProcedencia() {

    assertThrows(IllegalOperationException.class, () -> {

        EquipoEntity entity = equiposList.get(0);
        entity.setPaisProcedencia("");

        equipoService.updateEquipo(entity.getId(), entity);

    });
}

@Test
void createEquipoWithNullNombre() {
    assertThrows(IllegalOperationException.class, () -> {
        EquipoEntity pojoEntity = new EquipoEntity();
        pojoEntity.setId(1L);
        pojoEntity.setLogo("logo");
        pojoEntity.setBanderaPais("bandera");
        pojoEntity.setPaisProcedencia("pais");
        pojoEntity.setNombre(null);
        equipoService.createEquipo(pojoEntity);
    });
}


@Test
void createEquipoWithNullBanderaPais() {
    assertThrows(IllegalOperationException.class, () -> {
        EquipoEntity pojoEntity = new EquipoEntity();
        pojoEntity.setId(1L);
        pojoEntity.setLogo("logo");
        pojoEntity.setBanderaPais(null);
        pojoEntity.setPaisProcedencia("pais");
        pojoEntity.setNombre("nombre");
        equipoService.createEquipo(pojoEntity);
    });
}

@Test
void createEquipoWithEmptyStringBanderaPais() {
    assertThrows(IllegalOperationException.class, () -> {
        EquipoEntity pojoEntity = new EquipoEntity();
        pojoEntity.setId(1L);
        pojoEntity.setLogo("logo");
        pojoEntity.setBanderaPais("");
        pojoEntity.setPaisProcedencia("pais");
        pojoEntity.setNombre("nombre");
        equipoService.createEquipo(pojoEntity);
    });
}


@Test
void createEquipoWithNullLogo() {
    assertThrows(IllegalOperationException.class, () -> {
        EquipoEntity pojoEntity = new EquipoEntity();
        pojoEntity.setId(1L);
        pojoEntity.setLogo(null);
        pojoEntity.setBanderaPais("bandera");
        pojoEntity.setPaisProcedencia("pais");
        pojoEntity.setNombre("nombre");
        equipoService.createEquipo(pojoEntity);
    });
}

@Test
void testUpdateEquipoWithValidBanderaPais() {

    EquipoEntity unEquipo = equiposList.get(0);
    Long id = unEquipo.getId();;
    EquipoEntity pojoEntity= factory.manufacturePojo(EquipoEntity.class);
    pojoEntity.setId(1L);
    pojoEntity.setLogo("Logo");
    pojoEntity.setBanderaPais("bandera");
    pojoEntity.setPaisProcedencia("pais");
    pojoEntity.setNombre("nombre");
    pojoEntity.setCompetenciasParticipadas(competenciasList);
    
    assertDoesNotThrow(() -> {
        equipoService.updateEquipo(id, pojoEntity);
    }, MENSAJEBANDERA);
}

@Test
void testUpdateEquipoWithNonEmptyStringBanderaPais() {
    EquipoEntity unEquipo = equiposList.get(0);
    Long id = unEquipo.getId();
    EquipoEntity pojoEntity= factory.manufacturePojo(EquipoEntity.class);
    pojoEntity.setId(1L);
    pojoEntity.setLogo("Logo2");
    pojoEntity.setBanderaPais("bandera2");
    pojoEntity.setPaisProcedencia("pais2");
    pojoEntity.setNombre("nombre2");
    pojoEntity.setCompetenciasParticipadas(competenciasList);
    
    assertDoesNotThrow(() -> {
        equipoService.updateEquipo(id, pojoEntity);
    }, MENSAJEBANDERA);
}
@Test
void testUpdateEquipoWithValidNombre() {

    EquipoEntity unEquipo = equiposList.get(0);
    Long id = unEquipo.getId();
    EquipoEntity pojoEntity= factory.manufacturePojo(EquipoEntity.class);
    pojoEntity.setId(1L);
    pojoEntity.setLogo("Logo3");
    pojoEntity.setBanderaPais("bandera3");
    pojoEntity.setPaisProcedencia("pais3");
    pojoEntity.setNombre("nombre3");
    pojoEntity.setCompetenciasParticipadas(competenciasList);
    
    assertDoesNotThrow(() -> {
        equipoService.updateEquipo(id, pojoEntity);
    }, MENSAJENOMBRE);
}

@Test
void testUpdateEquipoWithNonEmptyStringNombre() {
    EquipoEntity unEquipo = equiposList.get(0);
    Long id = unEquipo.getId();
    EquipoEntity pojoEntity= factory.manufacturePojo(EquipoEntity.class);
    pojoEntity.setId(1L);
    pojoEntity.setLogo("Logo4");
    pojoEntity.setBanderaPais("bandera4");
    pojoEntity.setPaisProcedencia("pais4");
    pojoEntity.setNombre("nombre4");
    pojoEntity.setCompetenciasParticipadas(competenciasList);
    
    assertDoesNotThrow(() -> {
        equipoService.updateEquipo(id, pojoEntity);
    }, MENSAJENOMBRE);
}

}