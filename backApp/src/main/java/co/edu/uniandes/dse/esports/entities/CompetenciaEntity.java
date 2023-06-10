package co.edu.uniandes.dse.esports.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una competencia en un torneo
 *
 * @author
 */

 @Getter 
 @Setter
 @Entity
 @EqualsAndHashCode(callSuper = true)
public class CompetenciaEntity extends BaseEntity
{
    private String nombre;
    private String duracion;
    
    @PodamExclude
    @ManyToMany (mappedBy = "competenciasParticipadas")
    private List<EquipoEntity> equiposParticipantes = new ArrayList<>();

    @PodamExclude
    @OneToMany (mappedBy = "competenciaRelacionada")
    private List<ResultadoFinalEntity> resultadosFinales = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private EquipoEntity equipoGanador;

    @PodamExclude
    @ManyToOne
    private TorneoEntity torneo;

    @PodamExclude
    @ManyToOne
    private JugadorEntity jugador;

    @PodamExclude
    @ManyToOne
    private JugadorEntity mvp;

}
