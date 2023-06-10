package co.edu.uniandes.dse.esports.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa un libro en la persistencia
 *
 * @author 
 */

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
public class JugadorEntity extends BaseEntity {

        private String nombre;
        private String nickname;
        private String nacionalidad;
        private String fotografia;
        private Date fechaNacimiento;

        @PodamExclude
        @ManyToOne
        private EquipoEntity equipo;

        @PodamExclude
        @OneToMany(mappedBy = "jugador")
        private List<TorneoEntity> torneosParticipados = new ArrayList<>();

        @PodamExclude
        @OneToMany(mappedBy = "mvp")
        private List<CompetenciaEntity> competenciasMVP = new ArrayList<>();

        @PodamExclude
        @OneToMany(mappedBy = "jugador")
        private List<CompetenciaEntity> competenciasParticipadas = new ArrayList<>();

        @PodamExclude
        @OneToMany(mappedBy = "jugador")
        private List<LogrosEntity> logrosObtenidos = new ArrayList<>();
}

