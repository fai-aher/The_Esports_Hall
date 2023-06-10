package co.edu.uniandes.dse.esports.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa un torneo en la persistencia
 *
 * @author 
 */

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
public class TorneoEntity extends BaseEntity {
    
    private Date fechaFinalizacion;
    private String paisRealizacion;
    private String nombreTorneo;
    private String imagenRepresentativa;
    private String enlacePaginaWeb;
    private String organizador;
    private String videojuego;

    // Asociacion Torneo/Equipo. Equipo es la dueña de la asociacion.
    @PodamExclude
    @ManyToMany(mappedBy = "torneosParticipados")
    private List<EquipoEntity> equiposParticipantes = new ArrayList<>();

    // Asociacion Torneo/Competencia. Competencia es la dueña de la asociacion.
    @PodamExclude
    @OneToMany(mappedBy = "torneo", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CompetenciaEntity> competencias = new ArrayList<>();

    // Asociacion Torneo/Jugador. Torneo es la dueña de la asociacion.
    @PodamExclude
    @ManyToOne
    private JugadorEntity jugador;
}
