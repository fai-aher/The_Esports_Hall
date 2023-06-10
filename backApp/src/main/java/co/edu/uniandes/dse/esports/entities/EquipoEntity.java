package co.edu.uniandes.dse.esports.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa la entidad de un Equipo en la
 * persistencia de los datos de la aplicacion.
 *
 * @author fai-aher
 */

 @Getter
 @Setter
 @Entity
@EqualsAndHashCode(callSuper = true)

public class EquipoEntity extends BaseEntity {

    private String nombre;
    private String paisProcedencia;
    private String banderaPais;
    private String logo;
    
    //Asociacion Equipo - Torneo, Si le pertenece la asociacion
    @PodamExclude
    @ManyToMany
    private List<TorneoEntity> torneosParticipados = new ArrayList<>();

    //Asociacion Equipo - Competencia, Aqui la asociacion le pertenece
    @PodamExclude
    @ManyToMany
    private List<CompetenciaEntity> competenciasParticipadas = new ArrayList<>();

    //Asociacion Equipo - ResultadoFinal #2, Para conocer todos sus resultados finales en competencias.
    @PodamExclude
    @OneToMany(mappedBy = "equipoInvolucrado")
    private List<ResultadoFinalEntity> resultadosEnCompetencias = new ArrayList<>();

    //Asociacion Equipo - Competencia #2, Aqui no le pertenece la asociacion
    @PodamExclude
    @OneToMany(mappedBy = "equipoGanador") //No aplica orphan removal
    private List<CompetenciaEntity> competenciasGanadas = new ArrayList<>();

    //Asociacion Equipo - Jugador, No le pertenece la asociacion
    @PodamExclude
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.PERSIST, orphanRemoval = true) //En este caso, sí aplica orphanRemoval, pues
                                                                                        //en nuestro diseño de la aplicacion, no puede 
                                                                                 //haber un jugador sin que este asociado a un equipo.
    private List<JugadorEntity> integrantes = new ArrayList<>();





}
